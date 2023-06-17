package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(("/business/inter"))
public class InterfaceController {

    @Autowired
    private BaseHotelServiceImpl baseHotelService;
    @Value("${tencent.api.key}")
    private String key;
    @Autowired
    private TokenService tokenService;

    // TODO: 2023/4/1 此处接口只允许小程序端调用

    /**
     * 根据经纬度（，酒店名称）获得同城酒店列表
     *
     * @param map 地图
     * @return {@link AjaxResult}
     */
    @PostMapping("/list")
    public AjaxResult getHotelsByArea(@RequestBody HashMap<String,String> map,HttpServletRequest request) {

        /* 检查token */
        StaffUser staffUser = tokenService.getStaffUser(request);

        if(staffUser == null) {
            return AjaxResult.error("权限验证失败");
        }

        final String lat = map.get("lat");
        final String lng = map.get("lng");
        String level = map.get("level");

        /* 如果传来了酒店名称，则模糊查询酒店列表 */
        final String hotelName = map.get("hotelName");

        if(StringUtils.isEmpty(lat) || StringUtils.isEmpty(lng))
            return AjaxResult.error("经纬度不得为空");

        /* 默认为级别1，搜索该区下的酒店列表 */
        if(StringUtils.isEmpty(level))
            level = "1";
        if(!level.equals("1") && !level.equals("2") && !level.equals("3"))
            return AjaxResult.error("搜索级别错误");

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
        String adcode = adInfo.get("adcode").toString();

        switch (level) {
            case "2":{
                adcode = adcode.substring(0,4);
                break;
            }
            case "3":{
                adcode = adcode.substring(0,2);
                break;
            }
        }

        final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelByArea(adcode,hotelName);
        for (BaseHotel baseHotel : baseHotels) {
            final String latlng = baseHotel.getLatlng();
            final List<Double> doubles = parsePosition(latlng);
            final double v;
            try {
                v = StringUtils.GetDistance(Double.parseDouble(lng), Double.parseDouble(lat), doubles.get(1), doubles.get(0));
            }catch (Exception e) {
                return AjaxResult.error("距离计算错误");
            }
            baseHotel.setDistance(v);
        }
        return AjaxResult.success().put("list",baseHotels).put("total",baseHotels.size());
    }

    /**
     * 根据经纬度获取附近限定距离的所有酒店
     *
     * @param map 地图
     * @return {@link AjaxResult}
     */
    @PostMapping("/position")
    public AjaxResult getHotelsByPosition(@RequestBody HashMap<String,String> map,HttpServletRequest request) {

        /* 检查token */
        StaffUser staffUser = tokenService.getStaffUser(request);

        if(staffUser == null) {
            return AjaxResult.error("权限验证失败");
        }

        final double lat;
        final double lng;
        double limit;
        try {
            lat = Double.parseDouble(map.get("lat"));
            lng = Double.parseDouble(map.get("lng"));
            limit = Double.parseDouble(map.get("limit"));
        }catch (Exception e) {
            return AjaxResult.error("格式转换出错，请检查参数格式");
        }

        /* 如果传来了酒店名称，则模糊查询酒店列表 */
        final String hotelName = map.get("hotelName");

        final BaseHotel hotel = new BaseHotel();
        final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelList(hotel);
        final ArrayList<BaseHotel> list = new ArrayList<>();

        for (BaseHotel baseHotel : baseHotels) {
            final String latlng = baseHotel.getLatlng();
            final List<Double> doubles = parsePosition(latlng);
            final double v = StringUtils.GetDistance(lng, lat, doubles.get(1), doubles.get(0));
            if(v < limit) {
                if(!StringUtils.isEmpty(hotelName)) {
                    /* 过滤名称不符合的 */
                    if(!baseHotel.getHotelName().contains(hotelName))
                        continue;
                }
                baseHotel.setDistance(v);
                list.add(baseHotel);
            }
        }
        return AjaxResult.success().put("list",list).put("total",list.size());
    }



    /**
     * 解析位置获得经纬度
     *
     * @param latlng latlng
     * @return {@link List}<{@link Double}>
     */
    private List<Double> parsePosition(String latlng) {
        final String[] split = latlng.split(",");
        final Double aDouble = Double.valueOf(split[0]);
        final Double aDouble1 = Double.valueOf(split[1]);
        final ArrayList<Double> doubles = new ArrayList<>();
        doubles.add(aDouble);
        doubles.add(aDouble1);
        return doubles;
    }

}
