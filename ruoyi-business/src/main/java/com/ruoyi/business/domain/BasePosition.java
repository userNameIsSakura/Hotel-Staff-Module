package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 职位信息对象 base_position
 *
 * @author ruoyi
 * @date 2022-11-18
 */
public class BasePosition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职位ID */
    private Long positionId;

    /** 职位名 */
    @Excel(name = "职位名")
    private String positionName;

    /** 职能 */
    private  String functionValue;

    public String getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(String functionValue) {
        this.functionValue = functionValue;
    }

    public void setPositionId(Long positionId)
    {
        this.positionId = positionId;
    }

    public Long getPositionId()
    {
        return positionId;
    }
    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    public String getPositionName()
    {
        return positionName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("positionId", getPositionId())
            .append("positionName", getPositionName())
            .append("remark", getRemark())
            .toString();
    }
}
