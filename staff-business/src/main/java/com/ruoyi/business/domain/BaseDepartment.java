package com.ruoyi.business.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 部门信息对象 base_department
 *
 * @author ruoyi
 * @date 2022-11-12
 */
public class BaseDepartment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long departmentId;

    /** 上级ID */
    @Excel(name = "上级ID")
    private Long superiorId;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 部门名 */
    @Excel(name = "部门名")
    private String departmentName;

    /** 员工信息信息 */
    private List<BaseStaff> baseStaffList;

    public List<BasePosition> getBasePositionList() {
        return basePositionList;
    }

    public void setBasePositionList(List<BasePosition> basePositionList) {
        this.basePositionList = basePositionList;
    }

    /** 职位信息列表 */
    private List<BasePosition> basePositionList;

    public void setDepartmentId(Long departmentId)
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId()
    {
        return departmentId;
    }
    public void setSuperiorId(Long superiorId)
    {
        this.superiorId = superiorId;
    }

    public Long getSuperiorId()
    {
        return superiorId;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public List<BaseStaff> getBaseStaffList()
    {
        return baseStaffList;
    }

    public void setBaseStaffList(List<BaseStaff> baseStaffList)
    {
        this.baseStaffList = baseStaffList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("departmentId", getDepartmentId())
            .append("superiorId", getSuperiorId())
            .append("hotelId", getHotelId())
            .append("departmentName", getDepartmentName())
            .append("remark", getRemark())
            .append("baseStaffList", getBaseStaffList())
            .toString();
    }
}
