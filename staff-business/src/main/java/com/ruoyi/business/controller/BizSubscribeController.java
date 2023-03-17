package com.ruoyi.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.domain.SubscribeHotelRelationships;
import com.ruoyi.business.mapper.BaseHotelMapper;
import com.ruoyi.business.mqtt.MqttConfiguration;
import com.ruoyi.business.mqtt.MqttPushClient;
import com.ruoyi.business.service.ISubscribeHotelRelationshipsService;
import com.ruoyi.business.service.impl.BaseFunctionServiceImpl;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import com.ruoyi.business.utils.MqttConstantUtil;
import com.ruoyi.business.utils.SpringContextUtils;
import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BizSubscribe;
import com.ruoyi.business.service.IBizSubscribeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订阅信息Controller
 *
 * @author ruoyi
 * @date 2023-01-30
 */
@RestController
@RequestMapping("/business/subscribe")
public class BizSubscribeController extends BaseController
{
    @Autowired
    private IBizSubscribeService bizSubscribeService;
    @Autowired
    private ISubscribeHotelRelationshipsService subscribeHotelRelationshipsService;
    @Autowired
    private BaseHotelController baseHotelController;
    @Qualifier("mqttPushClient")
    @Autowired
    private MqttPushClient client;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private MqttConfiguration mqttConfiguration;

    private AtomicInteger topicId = new AtomicInteger(1);

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    /**
     *
     * 客户端获取数据接口
     *
     * */
    @PostMapping("/getDataList")
    public HashMap getDataList(@RequestBody HashMap<String,String> map) {

        String token = map.get("token");
        int pageSize = Integer.parseInt(map.get("pageSize"));
        int pageNum =   Integer.parseInt(map.get("pageNum"))  ;

        List<Object> cacheList = redisCache.getCacheList(token);
        int size = cacheList.size();
        Integer pageTotal = (size % pageSize == 0) ? size / pageSize : (size / pageSize) + 1 ;

        if(pageNum > pageTotal)
            return null;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total",size);

        if(pageNum == pageTotal) {
           hashMap.put("list",cacheList.subList((pageNum-1) * pageSize, size));
           return hashMap;
        }
        hashMap.put("list",cacheList.subList((pageNum-1) * pageSize ,  (pageNum-1) * pageSize + pageSize));
        return hashMap;
    }

    /**
     * 客户端请求接口
     *
     * @param map = {command:命令，parameter：参数}
     * @return success 请求成功
     *          denied  请求失败
     * */
    @PostMapping("/clientRequest")
    public AjaxResult clientRequest(@RequestBody HashMap<String,Object> map, HttpServletRequest request) {

        BaseHotel hotel = SpringContextUtils.getBean(BaseHotelServiceImpl.class).selectBaseHotelByHotelId(tokenService.getStaffUser(request).getHotelId());

        /* 检查token */
        StaffUser staffUser = tokenService.getStaffUser(request);

        if(staffUser == null) {
            return AjaxResult.error("权限验证失败");
        }

        /* 获得员工ID */
        String phone = staffUser.getPhone();

        /* token有效期验证 */
        tokenService.verifyStaffToken(staffUser);


        /* 命令 */
        String command = (String) map.get("command");
        /* 参数 */
        HashMap parameter = (HashMap) map.get("parameter");
        /* userId */
        String operationSystemUserId = phone.toString();
        /* 客户端topic */
        String topic = BaseStaffController.getClientTopic(phone);

        if(command.equals("")) {
            return AjaxResult.error("请求参数异常");
        }

        BizSubscribe bizSubscribe = new BizSubscribe();
        bizSubscribe.setSubscribeContent(command);

        /* 命令是否存在 */
        List<BizSubscribe> bizSubscribes = bizSubscribeService.selectBizSubscribeList(bizSubscribe);
        if(bizSubscribes.size() == 0) {
            return AjaxResult.error("当前请求不存在");
        }

        BizSubscribe commandObject = bizSubscribes.get(0);

        /* 是否可用 */
        if(commandObject.getAvailable() == 0) {
            return AjaxResult.error("当前请求不可用");
        }

        /* 全部酒店列表 */
        List<String> hotels = baseHotelController.listAllPrivate().stream().map(BaseHotel:: getHotelNumber).collect(Collectors.toList());

        /* 筛选黑名单 */
        List<SubscribeHotelRelationships> subscribeHotelRelationships = subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsByContent(command);
        List<String> blacklist = subscribeHotelRelationships.stream().map(SubscribeHotelRelationships::getHotelNumber).collect(Collectors.toList());

        /* 获得酒店列表 */
        if(blacklist.size() != 0 ) {
            hotels = hotels.stream().filter( h -> !blacklist.contains(h)).collect(Collectors.toList());
        }

        /* 限制参数 */
        if(parameter == null) {
            parameter = new HashMap();
        }
        /* 参数覆盖 */
        /* 酒店级限制参数 */
        /* 1.酒店押金 */
        parameter.put("hotelDeposit",hotel.getHotelDeposit());
        /* 2.免费早餐 */
        parameter.put("freeBreakfast",hotel.getHotelFreeBreakfast());
        /* 3.酒店结算时间 */
        parameter.put("clearingTime",hotel.getHotelSettlement());
        /* 4.房卡数量 */
        parameter.put("roomCardNumber",hotel.getHotelRoomCards());
        /* 权限级限制参数 */
        if(commandObject.getParameter() != null && !commandObject.getParameter().equals("")) {
            HashMap jsonObject = JSONObject.parseObject(commandObject.getParameter());
            parameter.putAll(jsonObject);
        }

        /* 获取返回Topic */
        String callbackTopic = getCallbackTopic();

        /* 服务端数据 */
        map = new HashMap<>();
        map.put("operationSystemUserId",operationSystemUserId);
        map.put("callbackTopic",callbackTopic);
        map.put("callbackTopicQos",1);
        map.put("operationCmd",command);
        /* 此处直接将map对象传入，不转换成JSON，否则会出现反斜杠 */
        map.put("data",parameter);

        HashMap<String, Object> redisCache = new HashMap<>();
        redisCache.put("responseNum", 0);
        redisCache.put("ifReturn", false);
        redisCache.put("clientTopic",topic);
        String token = Jwts.builder().setClaims(redisCache).signWith(SignatureAlgorithm.HS512,secret).compact();
        redisCache.put("token",token);
        redisCache.put("timestamp",System.currentTimeMillis());
        this.redisCache.setCacheMap(callbackTopic,redisCache);
        this.redisCache.expire(callbackTopic,MqttConstantUtil.DEFAULT_SURPLUS_EXPIRE);

        /* 更新缓存 */
        ArrayList<String> list = new ArrayList<>();
        list.add(callbackTopic);
        this.redisCache.setCacheList("clientTopics",list);
        this.redisCache.expire("clientTopics",MqttConstantUtil.DEFAULT_TOPICS_LIST_EXPIRE);

        mqttConfiguration.thread.interrupt();

        /* 发布消息 */
        for (String h : hotels) {
            map.put("operationHotelId",h);
            String serverTopic = "area/" + h.substring(0,2) +"/" + h.substring(2,4) + "/" + h.substring(4,6) + "/" + h.substring(6,10);
            System.out.println(serverTopic);
            /* 订阅 */
            client.subscribe(callbackTopic);
            System.out.println("订阅成功:"+callbackTopic);
            /* 发布 */
            System.out.println(map);
            client.publish(serverTopic,JSONObject.toJSONString(map).getBytes());
            System.out.println("发送数据：" + JSONObject.toJSONString(map));
        }
        return AjaxResult.success("success").put("token",token);
    }

    private String getCallbackTopic() {
        return "callback/"+ topicId.getAndIncrement();
    }


    /**
     * 查询订阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizSubscribe bizSubscribe)
    {
        startPage();
        List<BizSubscribe> list = bizSubscribeService.selectBizSubscribeList(bizSubscribe);
        return getDataTable(list);
    }

    /**
     * 查询订阅全部信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:list')")
    @GetMapping("/listAll")
    public List<BizSubscribe> listAll()
    {
        List<BizSubscribe> list = bizSubscribeService.selectBizSubscribeList(new BizSubscribe());
        return list;
    }


    /**
     * 导出订阅信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:export')")
    @Log(title = "订阅信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BizSubscribe bizSubscribe)
    {
        List<BizSubscribe> list = bizSubscribeService.selectBizSubscribeList(bizSubscribe);
        ExcelUtil<BizSubscribe> util = new ExcelUtil<BizSubscribe>(BizSubscribe.class);
        util.exportExcel(response, list, "订阅信息数据");
    }

    /**
     * 获取订阅信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:query')")
    @GetMapping(value = "/{subscribeId}")
    public AjaxResult getInfo(@PathVariable("subscribeId") Long subscribeId)
    {
        return AjaxResult.success(bizSubscribeService.selectBizSubscribeBySubscribeId(subscribeId));
    }

    /**
     * 新增订阅信息
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:add')")
    @Log(title = "订阅信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizSubscribe bizSubscribe)
    {
        return toAjax(bizSubscribeService.insertBizSubscribe(bizSubscribe));
    }

    /**
     * 修改订阅信息
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:edit')")
    @Log(title = "订阅信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizSubscribe bizSubscribe)
    {
        return toAjax(bizSubscribeService.updateBizSubscribe(bizSubscribe));
    }

    /**
     * 删除订阅信息
     */
    @PreAuthorize("@ss.hasPermi('business:subscribe:remove')")
    @Log(title = "订阅信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{subscribeIds}")
    public AjaxResult remove(@PathVariable Long[] subscribeIds)
    {
        return toAjax(bizSubscribeService.deleteBizSubscribeBySubscribeIds(subscribeIds));
    }
}
