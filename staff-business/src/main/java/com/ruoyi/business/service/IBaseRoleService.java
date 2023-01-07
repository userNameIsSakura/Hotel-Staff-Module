package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseRole;

/**
 * 角色信息Service接口
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public interface IBaseRoleService
{
    /**
     * 查询角色信息
     *
     * @param roleId 角色信息主键
     * @return 角色信息
     */
    public BaseRole selectBaseRoleByRoleId(Long roleId);

    /**
     * 查询角色信息列表
     *
     * @param baseRole 角色信息
     * @return 角色信息集合
     */
    public List<BaseRole> selectBaseRoleList(BaseRole baseRole);

    /**
     * 新增角色信息
     *
     * @param baseRole 角色信息
     * @return 结果
     */
    public int insertBaseRole(BaseRole baseRole);

    /**
     * 修改角色信息
     *
     * @param baseRole 角色信息
     * @return 结果
     */
    public int updateBaseRole(BaseRole baseRole);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色信息主键集合
     * @return 结果
     */
    public int deleteBaseRoleByRoleIds(Long[] roleIds);

    /**
     * 删除角色信息信息
     *
     * @param roleId 角色信息主键
     * @return 结果
     */
    public int deleteBaseRoleByRoleId(Long roleId);

    /**
     * 通过员工id查询角色
     *
     * @param staffId 员工id
     * @return {@link List}<{@link BaseRole}>
     */
    public List<BaseRole> selectBaseRoleByStaffId(Long staffId);
}
