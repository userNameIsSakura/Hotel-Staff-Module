package com.ruoyi.business.process;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.mqtt.MqttPushClient;
import com.ruoyi.business.utils.MqttConstantUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.TokenService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
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

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    private byte[] message;

    private String topic;

    @SneakyThrows
    @Override
    public void run() {
        try {

            String json = new String(message, "UTF-8");
            JSONObject jsonObject = JSONObject.parseObject(json);

            System.out.println("-------------------信息--------------------");
            System.out.println(jsonObject);
            System.out.println("------------------------------------------");

            int status = (int) jsonObject.get("code");
            String mes = (String) jsonObject.get("message");
            HashMap cache = (HashMap) redisCache.getCacheMap(topic);
            String clientTopic = (String) cache.get("clientTopic");

            if(status == 0) {
                /* 失败 */
                log.debug("服务端(" + topic +")异常:" + mes);
                return;
            }

            List<Object> array = (List<Object>) ((HashMap)jsonObject.get("data")).get("records");

            String token = (String) cache.get("token");
/*                if(token == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("clientTopic",topic);
                    hashMap.put("timeStamp",System.currentTimeMillis());
                    token = Jwts.builder().setClaims(hashMap).signWith(SignatureAlgorithm.HS512,secret).compact();
                    cache.put("token",token);
                    redisCache.setCacheMap(topic,cache);
                }else {
                    token = token.toString();
                }
                */
            if(array != null && array.size() > 0) {
                /* 这里set相当于在原有的list上add */
                redisCache.setCacheList((String) token,array);
                redisCache.expire((String) token, MqttConstantUtil.DEFAULT_TOKEN_EXPIRE);
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
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("status","success");
            map.put("token", (String) token);
            mqttPushClient.publish(clientTopic,JSONObject.toJSONString(map).getBytes());
            System.out.println("交付客户端Topic：" + clientTopic);

            /* 维护变量 */ /* 已弃用 */
//            int num = Integer.parseInt((String) redisCache.getCacheMap(topic).get("size")) ;
//            if(num == 1) {
//                /* 已收齐,返回token和数据总数 */
//                HashMap<String, String> map = new HashMap<>();
//                map.put("status","success");
//                map.put("token", (String) token);
//                map.put("total", String.valueOf(redisCache.getCacheList((String) token).size()));
//                mqttPushClient.publish(clientTopic,JSONObject.toJSONString(map).getBytes());
//            }else {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("size", String.valueOf(--num));
//                redisCache.setCacheMap(topic,map);
//                redisCache.expire(topic,MqttConstantUtil.DEFAULT_SURPLUS_EXPIRE);
//            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendReportRegularly() throws InterruptedException {

        synchronized (Thread.currentThread()) {

            while (true) {
                List<String> topics = redisCache.getCacheList("clientTopics");

                while (topics ==null || topics.size() == 0) {
                    /* 沉睡，等待唤醒 */
                    try {
                        TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                    }catch (InterruptedException e) {
//                        System.out.println("正常唤醒");
                        topics = redisCache.getCacheList("clientTopics");
                    }
                }

                log.debug("---------检察中-----------");
                for (String topic : topics) {

                    Map<String, Object> map = redisCache.getCacheMap(topic);
                    String clientTopic = (String) map.get("clientTopic");
                    String token = (String) map.get("token");
                    int responseNum = (int) map.get("responseNum");
                    HashMap<String, Object> returnMap = new HashMap<>();
                    returnMap.put("token",token);
                    returnMap.put("responseNum",responseNum);

                    if(System.currentTimeMillis() - (long) (map.get("timestamp")) > 10000 ) {
                        /* 更新缓存 */
                        log.debug("移除Topic:" + topic);
                        redisCache.deleteFromList("clientTopics",topic);
                    }
                    mqttPushClient.publish(clientTopic,JSONObject.toJSONString(returnMap).getBytes());
                }

                long l = System.currentTimeMillis();
                double i = 2;
                while (true) {
                    try{
                        TimeUnit.MILLISECONDS.sleep((long) (i*1000));
                    }catch (InterruptedException e) {
//                        System.out.println("异常打断（已解决");
                        i = 0.1;
                    }
                    if(System.currentTimeMillis() - l < 2000)
                        continue;
                    break;
                }

            }
        }
    }
}

