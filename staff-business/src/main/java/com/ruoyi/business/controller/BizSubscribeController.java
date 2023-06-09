package com.ruoyi.business.controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.domain.SubscribeHotelRelationships;
import com.ruoyi.business.domain.SysOperationLog;
import com.ruoyi.business.mapper.BaseHotelMapper;
import com.ruoyi.business.mqtt.MqttConfiguration;
import com.ruoyi.business.mqtt.MqttPushClient;
import com.ruoyi.business.process.DataReception;
import com.ruoyi.business.service.ISubscribeHotelRelationshipsService;
import com.ruoyi.business.service.SysOperationLogService;
import com.ruoyi.business.service.impl.BaseFunctionServiceImpl;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import com.ruoyi.business.utils.MqttConstantUtil;
import com.ruoyi.business.utils.SpringContextUtils;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    @Autowired
    private SysOperationLogService operationLogService;

    private AtomicLong topicId = new AtomicLong(1L);
    private static ConcurrentLinkedDeque receiveList = new ConcurrentLinkedDeque();

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    public static ConcurrentLinkedDeque getReceiveList() {
        return receiveList;
    }

    public static boolean remove(Object object) {
        return receiveList.remove(object);
    }

    public static ConcurrentHashMap<String,Thread> topicThreadMap = new ConcurrentHashMap<>();

    /**
     *
     * 客户端获取数据接口
     *
     * */
    @PostMapping("/getDataList")
    public HashMap getDataList(@RequestBody HashMap<String,String> map,HttpServletResponse response) {

        String token = map.get("token");
        int pageSize = Integer.parseInt(map.get("pageSize"));
        int pageNum =   Integer.parseInt(map.get("pageNum"))  ;

        HashMap<String, Object> hashMap = new HashMap<>();

        List<Object> cacheList = redisCache.getCacheList(token);

        int size = cacheList.size();
        Integer pageTotal = (size % pageSize == 0) ? size / pageSize : (size / pageSize) + 1 ;

        hashMap.put("code",HttpStatus.SUCCESS);
        hashMap.put("total",size);

        final Map<String, Object> cacheMap = redisCache.getCacheMap(token + "-param");
        /* 返回除了list之外的数据 */
        for (String s : cacheMap.keySet()) {
            hashMap.put(s,hashMap.get(s));
        }

        if(pageNum > pageTotal) {
            hashMap.put("list",new ArrayList<>());
        } else if(pageNum == pageTotal){
            hashMap.put("list",cacheList.subList((pageNum-1) * pageSize, size));
        } else {
            hashMap.put("list",cacheList.subList((pageNum-1) * pageSize ,  (pageNum-1) * pageSize + pageSize));
        }
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


        /* 检查token */
        StaffUser staffUser = tokenService.getStaffUser(request);

        if(staffUser == null) {
            return AjaxResult.error("权限验证失败");
        }

        BaseHotel hotel = null;

        if (!mqttConfiguration.getMqttPushClient().isConnected()) {
            return AjaxResult.error("MQTT未连接");
        }

        /* 获得员工ID */
        Long userId = staffUser.getUserId();

        /* token有效期验证 */
        tokenService.verifyStaffToken(staffUser);

        /* 命令 */
        String command = (String) map.get("command");
        /* 参数 */
        final Object para = map.get("parameter");
        HashMap parameterMap = null;
        ArrayList<HashMap> parameterArray = null;

        String paraType = "array";
        if(para instanceof HashMap) {
            parameterMap = (HashMap) para;
            paraType = "map";
        }
        else if(para instanceof ArrayList)
            parameterArray = (ArrayList<HashMap>) para;
        else
            return AjaxResult.error("parameter格式解析错误");

        /* userId */
        String operationSystemUserId = userId.toString();
        /* 客户端topic */
        String topic = staffUser.getTopic();
        /* 酒店编号 */
        final String operationHotelId = (String) map.get("operationHotelId");

        if(StringUtils.isNotNull(operationHotelId)) {
            final BaseHotel hotel1 = new BaseHotel();
            hotel1.setHotelNumber(operationHotelId);
            final List<BaseHotel> hotel2 = SpringContextUtils.getBean(BaseHotelServiceImpl.class).selectBaseHotelList(hotel1);
            if(hotel2.size() == 0)
                return AjaxResult.error("酒店编号不存在");
            hotel = hotel2.get(0);
        }

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

        /* 限制参数 */
        if(parameterMap == null) {
            parameterMap = new HashMap();
        }

        if(parameterArray == null) {
            parameterArray = new ArrayList<>();
        }


        /* 参数覆盖 */
        /* 权限级限制参数 */
        if(commandObject.getParameter() != null && !commandObject.getParameter().equals("")) {
            HashMap jsonObject = JSONObject.parseObject(commandObject.getParameter());
            if(paraType.equals("map")) {
                parameterMap.putAll(jsonObject);
            }else {
                try {
                    for (HashMap object : parameterArray) {
                        object.putAll(jsonObject);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    return AjaxResult.error("参数覆盖失败");
                }
            }
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
        map.put("data",paraType.equals("map") ? parameterMap : parameterArray);
        /* 酒店级限制参数 */
        if(hotel != null) {
            /* 1.酒店押金 */
            map.put("hotelDeposit",hotel.getHotelDeposit());
            /* 2.免费早餐 */
            map.put("freeBreakfast",hotel.getHotelFreeBreakfast());
            /* 3.酒店结算时间 */
            map.put("clearingTime",hotel.getHotelSettlement());
            /* 4.房卡数量 */
            map.put("roomCardNumber",hotel.getHotelRoomCards());
        }

        HashMap<String, Object> redisCache = new HashMap<>();
        redisCache.put("responseNum", 0);
        redisCache.put("ifReturn", false);
        redisCache.put("clientTopic",topic);
        redisCache.put("timestamp",System.currentTimeMillis());
        String token = Jwts.builder().setClaims(redisCache).signWith(SignatureAlgorithm.HS512,secret).compact();
        redisCache.put("token",token);
        this.redisCache.setCacheMap(callbackTopic,redisCache);
        this.redisCache.expire(callbackTopic,MqttConstantUtil.DEFAULT_SURPLUS_EXPIRE);

        /* 更新缓存 */
        ArrayList<String> list = new ArrayList<>();
        list.add(callbackTopic);

        /* 全部酒店列表 */
        List<String> hotels = baseHotelController.listAllPrivate().stream().map(BaseHotel:: getHotelNumber).collect(Collectors.toList());

        /* 筛选黑名单 */
        List<SubscribeHotelRelationships> subscribeHotelRelationships = subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsByContent(command);
        List<String> blacklist = subscribeHotelRelationships.stream().map(SubscribeHotelRelationships::getHotelNumber).collect(Collectors.toList());

        /* 获得酒店列表 */
        if(blacklist.size() != 0 ) {
            hotels = hotels.stream().filter( h -> !blacklist.contains(h)).collect(Collectors.toList());
        }

        /* 检查登录者所在酒店是否具有该权限 */
        if(!hotels.contains(SpringContextUtils.getBean(BaseHotelServiceImpl.class).selectBaseHotelByHotelId(staffUser.getHotelId()).getHotelNumber())) {
            return AjaxResult.error("您无权使用该命令");
        }

        /* 判断是否携带酒店编号 */
        if(StringUtils.isNotNull(operationHotelId)) {
            map.put("operationHotelId",operationHotelId);
            String serverTopic = "area/" + operationHotelId.substring(0,2) +"/" + operationHotelId.substring(2,4) + "/" + operationHotelId.substring(4,6) + "/" + operationHotelId.substring(6,10);
            System.out.println(serverTopic);
            /* 订阅 */
            client.subscribe(callbackTopic);
            System.out.println("订阅成功:"+callbackTopic);
            /* 发布 */
            System.out.println(map);
            client.publish(serverTopic,JSONObject.toJSONString(map).getBytes());
            System.out.println("发送数据：" + JSONObject.toJSONString(map));
        } else {
            /* 未携带：广播 */
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
        }

        final HashMap<String, Object> receive = new HashMap<>();
        receive.put("topic",callbackTopic);
        receive.put("time",System.currentTimeMillis());
        receiveList.add(receive);

        if(StringUtils.isNotEmpty(operationHotelId)) {
            hotels = new ArrayList<>();
            hotels.add(operationHotelId);
        }
        operationLog(userId, command, paraType.equals("map") ? parameterMap : parameterArray, StringUtils.isNotEmpty(operationHotelId), hotels);
        return AjaxResult.success().put("token",token);
    }

    /**
     * 操作日志
     *
     * @param userId  用户id
     * @param command 命令
     * @param data    数据
     */
    private void operationLog(Long userId, String command, Object data, boolean hotelId, List<String> hotels) {
        final SysOperationLog sysOperationLog = new SysOperationLog();
        sysOperationLog.setLogUser(userId);
        sysOperationLog.setLogCommand(command);
        sysOperationLog.setLogData(JSONObject.toJSONString(data));
        sysOperationLog.setLogType(hotelId ? 1 : 0);
        sysOperationLog.setLogTarget(JSONObject.toJSONString(hotels));
        sysOperationLog.setLogTime(new Date(System.currentTimeMillis()));
        operationLogService.insert(sysOperationLog);
    }

    /**
     * PMS前端请求接口
     *
     * @param map     参数
     * @param request 请求
     * @return {@link Object}
     */
    @PostMapping("/pmsRequest")
    public Object pmsRequest(@RequestBody HashMap<String,Object> map, HttpServletRequest request) {

        /* 检查token */
        StaffUser staffUser = tokenService.getStaffUser(request);

        if(staffUser == null) {
            return AjaxResult.error("权限验证失败");
        }

        if (!mqttConfiguration.getMqttPushClient().isConnected()) {
            return AjaxResult.error("MQTT未连接");
        }

        /* 命令 */
        String command = (String) map.get("command");
        /* 参数 */
        final Object para = map.get("parameter");
        HashMap parameterMap = null;
        ArrayList<HashMap> parameterArray = null;

        String paraType = "array";
        if(para instanceof HashMap) {
            parameterMap = (HashMap) para;
            paraType = "map";
        }
        else if(para instanceof ArrayList)
            parameterArray = (ArrayList<HashMap>) para;
        else
            return AjaxResult.error("parameter格式解析错误");
        /* 酒店编号 */
        final String operationHotelId = (String) map.get("operationHotelId");

        BizSubscribe bizSubscribe = new BizSubscribe();
        bizSubscribe.setSubscribeContent(command);
        /* 命令是否存在 */
        List<BizSubscribe> bizSubscribes = bizSubscribeService.selectBizSubscribeList(bizSubscribe);
        if(bizSubscribes.size() == 0) {
            return AjaxResult.error("当前请求不存在");
        }

        /* 是否可用 */
        BizSubscribe commandObject = bizSubscribes.get(0);
        if(commandObject.getAvailable() == 0) {
            return AjaxResult.error("当前请求不可用");
        }

        /* 限制参数 */
        if(parameterMap == null) {
            parameterMap = new HashMap();
        }

        if(parameterArray == null) {
            parameterArray = new ArrayList<>();
        }

        /* 参数覆盖 */
        if(commandObject.getParameter() != null && !commandObject.getParameter().equals("")) {
            HashMap jsonObject = JSONObject.parseObject(commandObject.getParameter());
            if(paraType.equals("map")) {
                parameterMap.putAll(jsonObject);
            }else {
                try {
                    for (HashMap object : parameterArray) {
                        object.putAll(jsonObject);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    return AjaxResult.error("参数覆盖失败");
                }
            }
        }

        /* 获取返回Topic */
        String callbackTopic = getPMSCallbackTopic();

        /* 服务端数据 */
        map = new HashMap<>();
        /* PMS的UserId为0 */
        map.put("operationSystemUserId",staffUser.getUserId());
        map.put("callbackTopic",callbackTopic);
        map.put("callbackTopicQos",1);
        map.put("operationCmd",command);
        /* 此处直接将map对象传入，不转换成JSON，否则会出现反斜杠 */
        map.put("data",paraType.equals("map") ? parameterMap : parameterArray);

        BaseHotel hotel = null;
        if(StringUtils.isNotNull(operationHotelId)) {
            final BaseHotel hotel1 = new BaseHotel();
            hotel1.setHotelNumber(operationHotelId);
            final List<BaseHotel> baseHotels = SpringContextUtils.getBean(BaseHotelServiceImpl.class).selectBaseHotelList(hotel1);
            if(baseHotels.size() == 0)
                return AjaxResult.error("不存在该酒店");
            hotel = baseHotels.get(0);
        }
        /* 酒店级限制参数 */
        if(hotel != null) {
            /* 1.酒店押金 */
            map.put("hotelDeposit",hotel.getHotelDeposit());
            /* 2.免费早餐 */
            map.put("freeBreakfast",hotel.getHotelFreeBreakfast());
            /* 3.酒店结算时间 */
            map.put("clearingTime",hotel.getHotelSettlement());
            /* 4.房卡数量 */
            map.put("roomCardNumber",hotel.getHotelRoomCards());
        }

        HashMap<String, Object> redisCache = new HashMap<>();
        redisCache.put("timestamp",System.currentTimeMillis());
        String token = Jwts.builder().setClaims(redisCache).signWith(SignatureAlgorithm.HS512,secret).compact();
        redisCache.put("token",token);
        this.redisCache.setCacheMap(callbackTopic,redisCache);
        this.redisCache.expire(callbackTopic,MqttConstantUtil.DEFAULT_SURPLUS_EXPIRE);

        /* 全部酒店列表 */
        List<String> hotels = baseHotelController.listAllPrivate().stream().map(BaseHotel:: getHotelNumber).collect(Collectors.toList());

        /* 筛选黑名单 */
        List<SubscribeHotelRelationships> subscribeHotelRelationships = subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsByContent(command);
        List<String> blacklist = subscribeHotelRelationships.stream().map(SubscribeHotelRelationships::getHotelNumber).collect(Collectors.toList());

        /* 获得酒店列表 */
        if(blacklist.size() != 0 ) {
            hotels = hotels.stream().filter( h -> !blacklist.contains(h)).collect(Collectors.toList());
        }

        /* 检查登录者所在酒店是否具有该权限 */
        if(!hotels.contains(SpringContextUtils.getBean(BaseHotelServiceImpl.class).selectBaseHotelByHotelId(staffUser.getHotelId()).getHotelNumber())) {
            return AjaxResult.error("您无权使用该命令");
        }

        topicThreadMap.put(callbackTopic,Thread.currentThread());
        logger.debug("topic" + callbackTopic + "已存入线程Map");

        /* 判断是否携带酒店编号 */
        if(StringUtils.isNotNull(operationHotelId)) {
            map.put("operationHotelId",operationHotelId);
            String serverTopic = "area/" + operationHotelId.substring(0,2) +"/" + operationHotelId.substring(2,4) + "/" + operationHotelId.substring(4,6) + "/" + operationHotelId.substring(6,10);
            System.out.println(serverTopic);
            /* 订阅 */
            client.subscribe(callbackTopic);
            System.out.println("订阅成功:"+callbackTopic);
            /* 发布 */
            System.out.println(map);
            client.publish(serverTopic,JSONObject.toJSONString(map).getBytes());
            System.out.println("发送数据：" + JSONObject.toJSONString(map));
        }else {
            return AjaxResult.error("未携带酒店编号");
        }

        final HashMap<String, Object> receive = new HashMap<>();
        receive.put("topic",callbackTopic);
        receive.put("time",System.currentTimeMillis());
        receiveList.add(receive);

        if(StringUtils.isNotEmpty(operationHotelId)) {
            hotels = new ArrayList<>();
            hotels.add(operationHotelId);
        }
        operationLog(staffUser.getUserId(), command, paraType.equals("map") ? parameterMap : parameterArray, StringUtils.isNotEmpty(operationHotelId), hotels);


        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            return DataReception.topicMsgMap.get(callbackTopic);
        }

        return AjaxResult.error("超时");
    }

    private String getCallbackTopic() {
        return "callback/"+ topicId.getAndIncrement();
    }

    private String getPMSCallbackTopic() {
        return "pmsCallback/"+ topicId.getAndIncrement();
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
