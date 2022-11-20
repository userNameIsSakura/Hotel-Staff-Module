package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 职能信息对象 base_function
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
public class BaseFunction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职能ID */
    private Long functionId;

    /** 职能 */
    @Excel(name = "职能")
    private String functionValue;

    public void setFunctionId(Long functionId) 
    {
        this.functionId = functionId;
    }

    public Long getFunctionId() 
    {
        return functionId;
    }
    public void setFunctionValue(String functionValue) 
    {
        this.functionValue = functionValue;
    }

    public String getFunctionValue() 
    {
        return functionValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("functionId", getFunctionId())
            .append("functionValue", getFunctionValue())
            .append("remark", getRemark())
            .toString();
    }
}
