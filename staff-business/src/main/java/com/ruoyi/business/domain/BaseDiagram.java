package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 介绍图类别对象 base_diagram
 * 
 * @author ruoyi
 * @date 2023-03-14
 */
public class BaseDiagram extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 介绍图类别 */
    private String diagramType;

    public void setDiagramType(String diagramType) 
    {
        this.diagramType = diagramType;
    }

    public String getDiagramType() 
    {
        return diagramType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("diagramType", getDiagramType())
            .toString();
    }
}
