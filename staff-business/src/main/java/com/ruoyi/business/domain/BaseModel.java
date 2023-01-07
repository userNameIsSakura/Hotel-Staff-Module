package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 范本列表对象 base_model
 *
 * @author ruoyi
 * @date 2022-12-03
 */
public class BaseModel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 范本ID */
    private Long modelId;

    /** 范本名 */
    @Excel(name = "范本名")
    private String modelName;

    /** 范本文件地址 */
    @Excel(name = "范本文件地址")
    private String modelFile;

    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }
    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getModelName()
    {
        return modelName;
    }
    public void setModelFile(String modelFile)
    {
        this.modelFile = modelFile;
    }

    public String getModelFile()
    {
        return modelFile;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("modelId", getModelId())
            .append("modelName", getModelName())
            .append("modelFile", getModelFile())
            .append("remark", getRemark())
            .toString();
    }
}
