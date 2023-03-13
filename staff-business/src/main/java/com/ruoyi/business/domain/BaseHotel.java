package com.ruoyi.business.domain;

import lombok.Data;
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
@Data
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

    /** 房卡数量 */
    @Excel(name = "房卡数量")
    private Long hotelRoomCards;

    /** 结算时间 */
    @Excel(name = "结算时间")
    private String hotelSettlement;

    /** 免费早餐数量 */
    @Excel(name = "免费早餐数量")
    private Long hotelFreeBreakfast;

    /** 押金 */
    @Excel(name = "押金")
    private Long hotelDeposit;

    /** 海报 */
    @Excel(name = "海报")
    private String hotelPoster;

    /** 酒店简介 */
    @Excel(name = "酒店简介")
    private String hotelIntroduct;

    /** 关联ID */
    @Excel(name = "关联ID")
    private Long chotelId;

    /** 父级ID */
    private Long chotelParent;

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


}
