package com.ruoyi.business.process;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.mqtt.MqttPushClient;
import com.ruoyi.business.utils.MqttConstantUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.framework.web.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

        String json = new String(message, "UTF-8");
        JSONObject jsonObject = JSONObject.parseObject(json);

        System.out.println("-------------------信息--------------------");
        System.out.println(jsonObject);
        System.out.println("------------------------------------------");

        /* 获取处理状态 */
        String status = (String) jsonObject.get("status");

        /* 错误信息 */
        if(status == null)
            return;

        HashMap cache = (HashMap) redisCache.getCacheMap(topic);
        String clientTopic = (String) cache.get("clientTopic");

        /* 服务端出错 */
        if(status.equals("error")) {
            HashMap<String, String> map = new HashMap<>();
            map.put("status","error");
            mqttPushClient.publish(clientTopic,JSONObject.toJSONString(map).getBytes());
            return;
        }


        List<Object> array = (List<Object>) jsonObject.get("data");

        /* 同步，防止缺少数据 */
        synchronized (DataReception.class) {

            Object token = redisCache.getCacheMap(topic).get("token");
            if(token == null) {
                /* 生成token */
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("clientTopic",topic);
                hashMap.put("timeStamp",System.currentTimeMillis());
                token = Jwts.builder().setClaims(hashMap).signWith(SignatureAlgorithm.HS512,secret).compact();
                cache.put("token",token);
                redisCache.setCacheMap(topic,cache);
            }else {
                token = token.toString();
            }

            if(array != null) {
                /* 这里set相当于在原有的list上add */
                redisCache.setCacheList((String) token,array);
                redisCache.expire((String) token, MqttConstantUtil.DEFAULT_TOKEN_EXPIRE);
            }

            /* 维护变量 */
            int num = Integer.parseInt((String) redisCache.getCacheMap(topic).get("size")) ;
            if(num == 1) {
                /* 已收齐,返回token和数据总数 */
                HashMap<String, String> map = new HashMap<>();
                map.put("status","success");
                map.put("token", (String) token);
                map.put("total", String.valueOf(redisCache.getCacheList((String) token).size()));
                mqttPushClient.publish(clientTopic,JSONObject.toJSONString(map).getBytes());
            }else {
                HashMap<String, String> map = new HashMap<>();
                map.put("size", String.valueOf(--num));
                redisCache.setCacheMap(topic,map);
                redisCache.expire(topic,MqttConstantUtil.DEFAULT_SURPLUS_EXPIRE);
            }
        }

    }
}

