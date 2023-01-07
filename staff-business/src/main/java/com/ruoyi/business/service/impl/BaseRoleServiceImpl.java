package com.ruoyi.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.business.domain.RoleAuthRelationships;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseRoleMapper;
import com.ruoyi.business.domain.BaseRole;
import com.ruoyi.business.service.IBaseRoleService;

/**
 * 角色信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-07
 */
@Service
public class BaseRoleServiceImpl implements IBaseRoleService
{
    @Autowired
    private BaseRoleMapper baseRoleMapper;


    @Override
    public List<BaseRole> selectBaseRoleByStaffId(Long staffId) {
        return baseRoleMapper.selectBaseRoleByStaffId(staffId);
    }

    /**
     * 查询角色信息
     *
     * @param roleId 角色信息主键
     * @return 角色信息
     */
    @Override
    public BaseRole selectBaseRoleByRoleId(Long roleId)
    {
        return baseRoleMapper.selectBaseRoleByRoleId(roleId);
    }

    /**
     * 查询角色信息列表
     *
     * @param baseRole 角色信息
     * @return 角色信息
     */
    @Override
    public List<BaseRole> selectBaseRoleList(BaseRole baseRole)
    {
        return baseRoleMapper.selectBaseRoleList(baseRole);
    }

    /**
     * 新增角色信息
     *
     * @param baseRole 角色信息
     * @return 结果
     */
    @Override
    public int insertBaseRole(BaseRole baseRole)
    {
        int i = baseRoleMapper.insertBaseRole(baseRole);
        /*新增角色权限关联*/
        insertRoleAuth(baseRole);
        return i;
    }

    /**
     * 修改角色信息
     *
     * @param baseRole 角色信息
     * @return 结果
     */
    @Override
    public int updateBaseRole(BaseRole baseRole)
    {
        int i = baseRoleMapper.updateBaseRole(baseRole);
        /*删除角色权限关联*/
        baseRoleMapper.deleteRoleAuthByRoleId(baseRole.getRoleId());
        /*新增角色权限关联*/
        insertRoleAuth(baseRole);
        return i;
    }

    public void insertRoleAuth(BaseRole baseRole) {
        Long[] auths = baseRole.getAuths();
        if(auths.length != 0) {
            List<RoleAuthRelationships> list = new ArrayList<>();
            for (Long auth : auths) {
                RoleAuthRelationships roleAuthRelationships = new RoleAuthRelationships();
                roleAuthRelationships.setAuthId(auth);
                roleAuthRelationships.setRoleId(baseRole.getRoleId());
                list.add(roleAuthRelationships);
            }
            baseRoleMapper.batchRoleAuth(list);
        }
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseRoleByRoleIds(Long[] roleIds)
    {
        return baseRoleMapper.deleteBaseRoleByRoleIds(roleIds);
    }

    /**
     * 删除角色信息信息
     *
     * @param roleId 角色信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseRoleByRoleId(Long roleId)
    {
        return baseRoleMapper.deleteBaseRoleByRoleId(roleId);
    }
}
