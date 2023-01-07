package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 员工信息对象 base_staff
 *
 * @author ruoyi
 * @date 2022-11-10
 */
public class BaseStaff extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工ID */
    private Long staffId;

    /** 员工名 */
    @Excel(name = "员工名")
    private String staffName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String staffPhone;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long departmentId;
    /** 部门名 */
    private String departmentName;
    /** 酒店名 */
    private String hotelName;
    /** 角色组 */
    private Long[] roles;

    public Long[] getRoles() {
        return roles;
    }

    public void setRoles(Long[] roles) {
        this.roles = roles;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /** 员工密码 */
    @Excel(name = "员工密码")
    private String staffPassword;

    /** 员工职位列表*/
    private List<BasePosition> basePositionList;

    public List<BasePosition> getBasePositionList() {
        return basePositionList;
    }

    public void setBasePositionList(List<BasePosition> basePositionList) {
        this.basePositionList = basePositionList;
    }

    public void setStaffId(Long staffId)
    {
        this.staffId = staffId;
    }

    public Long getStaffId()
    {
        return staffId;
    }
    public void setStaffName(String staffName)
    {
        this.staffName = staffName;
    }

    public String getStaffName()
    {
        return staffName;
    }
    public void setStaffPhone(String staffPhone)
    {
        this.staffPhone = staffPhone;
    }

    public String getStaffPhone()
    {
        return staffPhone;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }
    public void setDepartmentId(Long departmentId)
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId()
    {
        return departmentId;
    }
    public void setStaffPassword(String staffPassword)
    {
        this.staffPassword = staffPassword;
    }

    public String getStaffPassword()
    {
        return staffPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("staffId", getStaffId())
            .append("staffName", getStaffName())
            .append("staffPhone", getStaffPhone())
            .append("hotelId", getHotelId())
            .append("departmentId", getDepartmentId())
            .append("staffPassword", getStaffPassword())
            .append("remark", getRemark())
            .toString();
    }
}
