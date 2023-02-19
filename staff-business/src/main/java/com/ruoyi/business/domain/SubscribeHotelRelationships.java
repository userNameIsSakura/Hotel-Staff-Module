package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 鉴权信息对象 subscribe_hotel_relationships
 *
 * @author ruoyi
 * @date 2023-01-30
 */
public class SubscribeHotelRelationships extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 订阅ID */
    @Excel(name = "订阅ID")
    private Long subscribeId;

    /** 酒店编号 */
    private String hotelNumber;

    public String getHotelNumber() {
        return hotelNumber;
    }

    public void setHotelNumber(String hotelNumber) {
        this.hotelNumber = hotelNumber;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setSubscribeId(Long subscribeId)
    {
        this.subscribeId = subscribeId;
    }

    public Long getSubscribeId()
    {
        return subscribeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hotelId", getHotelId())
            .append("subscribeId", getSubscribeId())
            .toString();
    }
}
