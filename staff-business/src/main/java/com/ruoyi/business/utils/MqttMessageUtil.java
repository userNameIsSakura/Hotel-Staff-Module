package com.ruoyi.business.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageUtil {

    private final static String otaTopic = "ota_topic";
    public final static int MSG_TYPE_RESPONSE = 0;
    public final static int MSG_TYPE_REQUEST = 1;

}
