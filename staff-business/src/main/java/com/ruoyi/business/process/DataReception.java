package com.ruoyi.business.process;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.controller.BizSubscribeController;
import com.ruoyi.business.mqtt.MqttPushClient;
import com.ruoyi.business.utils.MqttMessageUtil;
import com.ruoyi.business.websocket.WebSocket;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * @program: mqtt
 * @description: 解析报文
 * @author: fzw
 * @create: 2022-05-18 16:31
 **/
@Data
@Slf4j
@Component
@Scope("prototype")
public class DataReception implements Runnable{

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MqttPushClient mqttPushClient;
    @Autowired
    private WebSocket webSocket;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;
    private byte[] message;
    private String topic;
    private static ConcurrentLinkedDeque<HashMap> msgList = new ConcurrentLinkedDeque<>();

    public static ConcurrentHashMap<String,Object> topicMsgMap = new ConcurrentHashMap<>();

    @SneakyThrows
    @Override
    public void run() {
        try {
            String json = new String(message, StandardCharsets.UTF_8);

            JSONObject jsonObject = JSONObject.parseObject(json);

            System.out.println("-------------------信息--------------------");
            System.out.println(jsonObject);
            System.out.println("------------------------------------------");

            /* 同步请求数据到达 */
            if(topic.startsWith("sync")) {
                if(jsonObject == null)
                    jsonObject = new JSONObject();
                topicMsgMap.put(topic,jsonObject);
                if(BizSubscribeController.getTopicThreadMap().get(topic) != null)
                    BizSubscribeController.getTopicThreadMap().get(topic).interrupt();
                else
                    log.error("pms线程为空");
                return;
            }

            int status = (int) jsonObject.get("code");
            String mes = (String) jsonObject.get("message");
            HashMap cache = (HashMap) redisCache.getCacheMap(topic);
            String clientTopic = (String) cache.get("clientTopic");

            boolean flag = false;
            final ConcurrentLinkedDeque receiveList = BizSubscribeController.getReceiveList();
            for (Object o : receiveList) {
                HashMap<String,Object> map = (HashMap<String, Object>) o;
                if(map.get("topic").equals(topic)) {
                    if((((Long)(map.get("time"))) + 10 * 1000 > System.currentTimeMillis())) {
                        /* 未过期 */
                        flag = true;
                        break;
                    }else {
                        /* 已过期 */
                        /* 取消订阅 */
                        mqttPushClient.unSubscribe(topic);
                        BizSubscribeController.remove(o);
                    }
                }
            }

            /* 信息已丢弃或已过期 */
            if(!flag) {
                log.debug("信息已过期");
                return;
            }

            if(status == 0) {
                /* 失败 */
                log.debug("服务端(" + topic +")异常:" + mes);
            }

            String token = (String) cache.get("token");

            if(StringUtils.isNotNull(jsonObject.get("data"))) {

                JSONArray array = new JSONArray();

                final Object data = jsonObject.get("data");
                if(data instanceof JSONArray) {
                    array = (JSONArray) data;
                }else if(data instanceof  JSONObject) {
                    array = new JSONArray();
                    array.add(data);
                }
                redisCache.setCacheList(token,array);
            }

            /* 同步 */
            synchronized (DataReception.class) {
                cache = (HashMap) redisCache.getCacheMap(topic);

                boolean ifReturn = (boolean) cache.get("ifReturn");
                int responseNum = (int) cache.get("responseNum");
                HashMap<String, Object> hashMap = new HashMap<>();
                /* 反馈酒店数量更新 */
                hashMap.put("responseNum",++responseNum);
                log.debug("responseNum:" + responseNum);
                if(!ifReturn) {
                    /* 已告知标识位 */
                    hashMap.put("ifReturn",true);
                }
                redisCache.setCacheMap(topic,hashMap);

                HashMap<String, Object> returnMap = new HashMap<>();
                returnMap.put("token",token);
                returnMap.put("responseNum",responseNum);

                final HashMap map = new HashMap();
                map.put("topic",clientTopic);
                map.put("msg",JSONObject.toJSONString(returnMap).getBytes());
                /* 标识位设为0表示是返回信息 */
                map.put("type", MqttMessageUtil.MSG_TYPE_RESPONSE);
                msgList.add(map);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendReportRegularly() throws InterruptedException {
        while (true) {
            int size = msgList.size();
            final HashMap<String, byte[]> map = new HashMap<>();
            for (int i = 0; i < size; i++) {
                final HashMap hashMap = msgList.removeFirst();
                map.put((String) hashMap.get("topic"),(byte[]) hashMap.get("msg"));
            }
            for (String s : map.keySet()) {
                System.out.println("发送主题：" + s);
                mqttPushClient.publish(s,map.get(s));
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
