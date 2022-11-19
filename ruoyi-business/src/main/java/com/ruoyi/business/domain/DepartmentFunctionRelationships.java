package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 部门职能连接信息对象 department_function_relationships
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
public class DepartmentFunctionRelationships extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long departmentId;

    /** 职位ID */
    @Excel(name = "职位ID")
    private Long positionId;

    /** 职能ID */
    @Excel(name = "职能ID")
    private Long functionId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }
    public void setPositionId(Long positionId) 
    {
        this.positionId = positionId;
    }

    public Long getPositionId() 
    {
        return positionId;
    }
    public void setFunctionId(Long functionId) 
    {
        this.functionId = functionId;
    }

    public Long getFunctionId() 
    {
        return functionId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("departmentId", getDepartmentId())
            .append("positionId", getPositionId())
            .append("functionId", getFunctionId())
            .toString();
    }
}
