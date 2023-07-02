package com.ruoyi.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.annotations.StaffTokenCheck;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.domain.HotelSelectParam;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店列表Controller
 *
 * @author ruoyi
 * @date 2023-01-06
 */
@RestController
@RequestMapping("/business/hotel")
public class BaseHotelController extends BaseController
{

    @Value("${tencent.api.key}")
    private String key;
    @Value("${ruoyi.profile}")
    private String uploadPathPrefix;

    @Autowired
    private IBaseHotelService baseHotelService;
    @Autowired
    private IBaseChainHotelService baseChainHotelService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TokenService tokenService;
    private String positionRedisKey = "hotels";


    /**
     * 对模块酒店查询接口 （同城 + 距离 + 酒店信息）
     * */
    @PostMapping("/select")
    @StaffTokenCheck
    public AjaxResult selectHotel(@RequestBody HotelSelectParam hotelSelectParam, HttpServletRequest request) {

        List<Long> hotelIds = new ArrayList<>();

        /* 若涉及到范围查询或者同城查询，则需要检查经纬度是否不为空 */
        if(hotelSelectParam.getDistance() != null || hotelSelectParam.getLevel() != null) {
            /* 必须包含的参数：经纬度 */
            if(hotelSelectParam.getLat() == null)
                return AjaxResult.error("纬度不得为空");
            if(hotelSelectParam.getLng() == null)
                return AjaxResult.error("经度不得为空");
        }

        final Double lat = hotelSelectParam.getLat();
        final Double lng = hotelSelectParam.getLng();

        if(hotelSelectParam.getDistance() != null) {
            /* 限制距离,通过Redis快速查询范围内的酒店ID */
            final double distance = hotelSelectParam.getDistance();
            final GeoOperations geo = redisTemplate.opsForGeo();
            /* 查询范围内所有酒店ID */
            final GeoResults<RedisGeoCommands.GeoLocation<Long>> hotels = geo.radius(positionRedisKey, new Circle(new Point(lng, lat), new Distance(distance,Metrics.NEUTRAL)));
            for (GeoResult<RedisGeoCommands.GeoLocation<Long>> longGeoResult : hotels.getContent()) {
                hotelIds.add(longGeoResult.getContent().getName());
            }
        }else {
            hotelIds = baseHotelService.selectBaseHotelList(new BaseHotel()).stream().map(BaseHotel::getHotelId).collect(Collectors.toList());
        }

        if(hotelIds.contains(0L))
            hotelIds.remove(0L);

        /* 如果酒店ID组为空，直接返回 */
        if(hotelIds.size() == 0)
            return AjaxResult.success(new ArrayList<>());

        /* 根据酒店信息查询 */
        /* 根据Level是否为空判断是否需要同城查询（酒店地域编号识别） */
        if(hotelSelectParam.getHotel() == null)
            hotelSelectParam.setHotel(new BaseHotel());
        final BaseHotel baseHotel = hotelSelectParam.getHotel();

        /* 同城查询编号，默认不查同城 */
        String adcode = "";
        if(hotelSelectParam.getLevel() != null) {
            final Integer level = hotelSelectParam.getLevel();
            /* 不为空，即需要同城查询 */
            String url = "https://apis.map.qq.com/ws/geocoder/v1/?location="+lat+","+lng+"&key="+ key +"&get_poi=1";
            final String s = HttpUtils.sendGet(url);
            if(StringUtils.isEmpty(s))
                return AjaxResult.error("地址信息查询失败");
            final JSONObject jsonObject = JSONObject.parseObject(s);
            if(!jsonObject.get("status").toString().equals("0"))
                return AjaxResult.error("地址信息查询失败");
            final String result = jsonObject.get("result").toString();
            final JSONObject resultO = JSONObject.parseObject(result);
            final String ad_info = resultO.get("ad_info").toString();
            final JSONObject adInfo = JSONObject.parseObject(ad_info);
            adcode = adInfo.get("adcode").toString();

            switch (level) {
                case 1: {
                    break;
                }
                case 2:{
                    adcode = adcode.substring(0,4);
                    break;
                }
                case 3:{
                    adcode = adcode.substring(0,2);
                    break;
                }
                default:{
                    return AjaxResult.error("Level参数不合法（合法值：1，2，3）");
                }
            }
        }
        /* 查询结果并返回 */
        final HashMap<String, Object> map = new HashMap<>();
        map.put("hotel",baseHotel);
        map.put("hotelIds",hotelIds);
        map.put("adcode",adcode);
        System.out.println("查询" + map);
        final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelForOutside(map);

        if(lat != null && lng != null) {
            /* 携带了经纬度，需要计算距离 */
            String uuid = String.valueOf(UUID.randomUUID());
            final GeoOperations geo = redisTemplate.opsForGeo();
            geo.add(positionRedisKey,new Point(lng,lat),uuid);
            try {
                for (BaseHotel hotel : baseHotels) {
                    final Distance hotels = geo.distance(positionRedisKey, hotel.getHotelId(), uuid);
                    hotel.setDistance(hotels.getValue());
                }
            }finally {
                geo.remove(positionRedisKey,uuid);
            }
        }

        return AjaxResult.success(baseHotels);
    }






    /**
     * 查询酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseHotel baseHotel)
    {
        startPage();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return getDataTable(list);
    }


    /**
     * 查询酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/listAll")
    public List<BaseHotel> listAll()
    {
        BaseHotel baseHotel = new BaseHotel();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return list;
    }


    /**
     * 查询集团下酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/listHotels")
    public List<BaseHotel> listHotelAll()
    {
        final BaseChainHotel baseChainHotel = new BaseChainHotel();
        baseChainHotel.setChotelParent(SecurityUtils.getHotelId());
        final List<BaseChainHotel> baseChainHotels = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);
        final ArrayList<BaseHotel> baseHotels = new ArrayList<>();
        for (BaseChainHotel chainHotel : baseChainHotels) {
            final BaseHotel hotel = baseHotelService.selectBaseHotelByChotelId(chainHotel.getChotelId());
            baseHotels.add(hotel);
        }
        return baseHotels;
    }


    public List<BaseHotel> listAllPrivate()
    {
        BaseHotel baseHotel = new BaseHotel();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return list;
    }

    /**
     * 导出酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:export')")
    @Log(title = "酒店列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseHotel baseHotel) {
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        ExcelUtil<BaseHotel> util = new ExcelUtil<BaseHotel>(BaseHotel.class);
        util.exportExcel(response, list, "酒店列表数据");
    }

    /**
     * 获取酒店列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:query')")
    @GetMapping(value = "/{hotelId}")
    public AjaxResult getInfo(@PathVariable("hotelId") Long hotelId)
    {
        return AjaxResult.success(baseHotelService.selectBaseHotelByHotelId(hotelId));
    }

    /**
     * 获取酒店列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:query')")
    @GetMapping(value = "/chotelId/{chotelId}")
    public AjaxResult getInfoByChotelId(@PathVariable("chotelId") Long chotelId)
    {
        return AjaxResult.success(baseHotelService.selectBaseHotelByChotelId(chotelId));
    }

    /**
     * 新增酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:add')")
    @Log(title = "酒店列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseHotel baseHotel)
    {
        final BaseHotel baseHotel1 = new BaseHotel();
        baseHotel1.setHotelName(baseHotel.getHotelName());
        if ((baseHotelService.selectBaseHotelList(baseHotel1).size() > 0)) {
            return AjaxResult.error("酒店名已存在");
        }
        return toAjax(baseHotelService.insertBaseHotel(baseHotel));
    }

    /**
     * 修改酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:edit')")
    @Log(title = "酒店列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseHotel baseHotel)
    {
        return toAjax(baseHotelService.updateBaseHotel(baseHotel));
    }

    /**
     * 删除酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:remove')")
    @Log(title = "酒店列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{hotelIds}")
    public AjaxResult remove(@PathVariable Long[] hotelIds)
    {
        return toAjax(baseHotelService.deleteBaseHotelByHotelIds(hotelIds));
    }
}
