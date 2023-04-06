package com.ruoyi.common.core.domain.model;

public class StaffUser {


    /**
     * 员工ID
     * */
    private String phone;

    /**
    * 唯一标识
    * */
    private String token;

    /**
     * 过期时间
     * */
    private Long expireTime;

    /**
    * 酒店ID
    * */
    private Long hotelId;

    /**
     * topic
     * */
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
