package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 酒店列表对象 base_hotel
 *
 * @author ruoyi
 * @date 2023-01-06
 */
public class BaseHotel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 酒店ID */
    private Long hotelId;

    /** 酒店名 */
    @Excel(name = "酒店名")
    private String hotelName;

    /** 酒店编号 */
    @Excel(name = "酒店编号")
    private String hotelNumber;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String area;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public String getHotelName()
    {
        return hotelName;
    }
    public void setHotelNumber(String hotelNumber)
    {
        this.hotelNumber = hotelNumber;
    }

    public String getHotelNumber()
    {
        return hotelNumber;
    }

    @Override
    public String toString() {
        return "BaseHotel{" +
                "hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", hotelNumber='" + hotelNumber + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                '}';
    }
}
