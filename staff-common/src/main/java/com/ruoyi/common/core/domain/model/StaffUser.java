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
