package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 连锁酒店对象 base_chain_hotel
 * 
 * @author ruoyi
 * @date 2023-03-13
 */
public class BaseChainHotel extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 连锁酒店ID */
    private Long chotelId;

    /** 上级ID */
    @Excel(name = "上级ID")
    private Long chotelParent;

    /** 酒店名 */
    @Excel(name = "酒店名")
    private String chotelName;

    public void setChotelId(Long chotelId) 
    {
        this.chotelId = chotelId;
    }

    public Long getChotelId() 
    {
        return chotelId;
    }
    public void setChotelParent(Long chotelParent) 
    {
        this.chotelParent = chotelParent;
    }

    public Long getChotelParent() 
    {
        return chotelParent;
    }
    public void setChotelName(String chotelName) 
    {
        this.chotelName = chotelName;
    }

    public String getChotelName() 
    {
        return chotelName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("chotelId", getChotelId())
            .append("chotelParent", getChotelParent())
            .append("chotelName", getChotelName())
            .append("remark", getRemark())
            .toString();
    }
}
