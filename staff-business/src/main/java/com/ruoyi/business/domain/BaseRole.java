package com.ruoyi.business.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 角色信息对象 base_role
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public class BaseRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long roleId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 酒店ID */
    @Excel(name = "酒店ID")
    private Long hotelId;

    /** 权限组 */
    private Long[] auths;

    public Long[] getAuths() {
        return auths;
    }

    public void setAuths(Long[] auths) {
        this.auths = auths;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setHotelId(Long hotelId)
    {
        this.hotelId = hotelId;
    }

    public Long getHotelId()
    {
        return hotelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("hotelId", getHotelId())
            .toString();
    }
}
