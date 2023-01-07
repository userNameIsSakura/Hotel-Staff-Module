package com.ruoyi.business.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 合同记录对象 biz_contract
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public class BizContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 合同id */
    private Long contractId;

    /** 酒店id */
    @Excel(name = "酒店id")
    private Long hotelId;

    /** 合同名 */
    @Excel(name = "合同名")
    private String contractName;

    /** 合同文件地址 */
    @Excel(name = "合同文件地址")
    private String contractFile;

    /** 签约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractSign;

    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生效时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractEffect;

    /** 到期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date contractInvalid;

    /** 合同状态 */
    @Excel(name = "合同状态")
    private String contractState;

    /** 范本id */
    @Excel(name = "范本id")
    private Long modelId;

    public void setContractId(Long contractId)
    {
        this.contractId = contractId;
    }

    public Long getContractId()
    {
        return contractId;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getContractName()
    {
        return contractName;
    }
    public void setContractFile(String contractFile)
    {
        this.contractFile = contractFile;
    }

    public String getContractFile()
    {
        return contractFile;
    }
    public void setContractSign(Date contractSign)
    {
        this.contractSign = contractSign;
    }

    public Date getContractSign()
    {
        return contractSign;
    }
    public void setContractEffect(Date contractEffect)
    {
        this.contractEffect = contractEffect;
    }

    public Date getContractEffect()
    {
        return contractEffect;
    }
    public void setContractInvalid(Date contractInvalid)
    {
        this.contractInvalid = contractInvalid;
    }

    public Date getContractInvalid()
    {
        return contractInvalid;
    }
    public void setContractState(String contractState)
    {
        this.contractState = contractState;
    }

    public String getContractState()
    {
        return contractState;
    }
    public void setModelId(Long modelId)
    {
        this.modelId = modelId;
    }

    public Long getModelId()
    {
        return modelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("contractId", getContractId())
            .append("hotelId", getHotelId())
            .append("contractName", getContractName())
            .append("contractFile", getContractFile())
            .append("contractSign", getContractSign())
            .append("contractEffect", getContractEffect())
            .append("contractInvalid", getContractInvalid())
            .append("remark", getRemark())
            .append("contractState", getContractState())
            .append("modelId", getModelId())
            .toString();
    }
}
