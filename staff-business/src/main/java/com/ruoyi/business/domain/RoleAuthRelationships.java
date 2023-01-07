package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色权限关联信息对象 role_auth_relationships
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public class RoleAuthRelationships extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 角色ID */
    @Excel(name = "角色ID")
    private Long roleId;

    /** 权限ID */
    @Excel(name = "权限ID")
    private Long authId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setAuthId(Long authId)
    {
        this.authId = authId;
    }

    public Long getAuthId()
    {
        return authId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("roleId", getRoleId())
                .append("authId", getAuthId())
                .toString();
    }
}
