package com.ruoyi.business.mqtt;

import com.ruoyi.business.process.DataReception;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(MqttConfiguration.PREFIX)
public class MqttConfiguration implements ApplicationRunner {
    @Autowired
    private MqttPushClient mqttPushClient;
    @Autowired
    private DataReception dataReception;

    public static final String PREFIX = "mqtt.broker";
    private String[] host;
    private String clientid;
    private String username;
    private String password;
    private String serverTopic;
    private int qos;
    private int timeout;
    private int keepalive;

    /* 启动MQTT异步消息通知线程 */
    public final Thread thread = new Thread(() -> {
        try {
            this.dataReception.sendReportRegularly();
        }catch (Exception e) {
            e.printStackTrace();
        }
    });

    @Bean
    public MqttPushClient getMqttPushClient() {
        return mqttPushClient;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        connect();
        thread.start();
    }



    public void connect() {
        mqttPushClient.connect(host, clientid, username, password, timeout, keepalive, qos);
    }
}
