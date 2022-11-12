package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 base_administrators
 *
 * @author ruoyi
 * @date 2022-11-10
 */
public class SysAdministrator extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long administratorId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long hotelId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String administratorName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String administratorPhone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer superAdministrator;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String password;

    public void setAdministratorId(Long administratorId)
    {
        this.administratorId = administratorId;
    }

    public Long getAdministratorId()
    {
        return administratorId;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setAdministratorName(String administratorName)
    {
        this.administratorName = administratorName;
    }

    public String getAdministratorName()
    {
        return administratorName;
    }
    public void setAdministratorPhone(String administratorPhone)
    {
        this.administratorPhone = administratorPhone;
    }

    public String getAdministratorPhone()
    {
        return administratorPhone;
    }
    public void setSuperAdministrator(Integer superAdministrator)
    {
        this.superAdministrator = superAdministrator;
    }

    public Integer getSuperAdministrator()
    {
        return superAdministrator;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("administratorId", getAdministratorId())
                .append("hotelId", getHotelId())
                .append("administratorName", getAdministratorName())
                .append("administratorPhone", getAdministratorPhone())
                .append("superAdministrator", getSuperAdministrator())
                .append("password", getPassword())
                .append("remark", getRemark())
                .toString();
    }
}
