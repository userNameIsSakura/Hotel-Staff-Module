package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 酒店介绍图对象 hotel_disgram_relationships
 * 
 * @author ruoyi
 * @date 2023-03-14
 */
public class HotelDisgramRelationships extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 介绍图类别 */
    @Excel(name = "介绍图类别")
    private String diagramType;

    /** 介绍图路径 */
    @Excel(name = "介绍图路径")
    private String diagramPath;

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
    public void setDiagramType(String diagramType) 
    {
        this.diagramType = diagramType;
    }

    public String getDiagramType() 
    {
        return diagramType;
    }
    public void setDiagramPath(String diagramPath) 
    {
        this.diagramPath = diagramPath;
    }

    public String getDiagramPath() 
    {
        return diagramPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("hotelId", getHotelId())
            .append("diagramType", getDiagramType())
            .append("diagramPath", getDiagramPath())
            .append("remark", getRemark())
            .toString();
    }
}
