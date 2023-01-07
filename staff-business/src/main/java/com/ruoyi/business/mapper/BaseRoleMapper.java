package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BaseRole;
import com.ruoyi.business.domain.RoleAuthRelationships;
import com.ruoyi.system.domain.SysUserRole;

/**
 * 角色信息Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public interface BaseRoleMapper
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
     * 删除角色信息
     *
     * @param roleId 角色信息主键
     * @return 结果
     */
    public int deleteBaseRoleByRoleId(Long roleId);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseRoleByRoleIds(Long[] roleIds);

    /**
     * 通过角色id删除角色权限关联
     *
     * @param roleId 角色id
     * @return int
     */
    public int deleteRoleAuthByRoleId(Long roleId);

    /**
     * 批量新增角色权限信息
     *
     * @param roleAuthRelationships 用户角色列表
     * @return 结果
     */
    public int batchRoleAuth(List<RoleAuthRelationships> roleAuthRelationships);


    /**
     * 通过员工id查询角色
     *
     * @param staffId 员工id
     * @return {@link List}<{@link BaseRole}>
     */
    public List<BaseRole> selectBaseRoleByStaffId(Long staffId);
}
