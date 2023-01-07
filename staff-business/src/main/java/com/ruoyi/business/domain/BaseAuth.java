package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 权限信息对象 base_auth
 * 
 * @author ruoyi
 * @date 2023-01-07
 */
public class BaseAuth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long authId;

    /** URL */
    @Excel(name = "URL")
    private String url;

    /** 权限名 */
    @Excel(name = "权限名")
    private String authName;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    public void setAuthId(Long authId) 
    {
        this.authId = authId;
    }

    public Long getAuthId() 
    {
        return authId;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setAuthName(String authName) 
    {
        this.authName = authName;
    }

    public String getAuthName() 
    {
        return authName;
    }
    public void setHotelId(Long hotelId) 
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId() 
    {
        return hotelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("authId", getAuthId())
            .append("url", getUrl())
            .append("authName", getAuthName())
            .append("hotelId", getHotelId())
            .toString();
    }
}
