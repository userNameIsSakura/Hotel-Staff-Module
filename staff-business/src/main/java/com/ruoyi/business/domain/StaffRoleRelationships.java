package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工角色关联信息对象 staff_role_relationships
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public class StaffRoleRelationships extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 员工ID */
    @Excel(name = "员工ID")
    private Long staffId;

    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setStaffId(Long staffId)
    {
        this.staffId = staffId;
    }

    public Long getStaffId()
    {
        return staffId;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("staffId", getStaffId())
                .append("roleId", getRoleId())
                .toString();
    }
}
