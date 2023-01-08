package com.ruoyi.common.core.domain.model;

public class StaffUser {


    /**
     * 员工ID
     * */
    private Long staffId;

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

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
