package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.DepartmentFunctionRelationshipsMapper;
import com.ruoyi.business.domain.DepartmentFunctionRelationships;
import com.ruoyi.business.service.IDepartmentFunctionRelationshipsService;

/**
 * 部门职能连接信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
@Service
public class DepartmentFunctionRelationshipsServiceImpl implements IDepartmentFunctionRelationshipsService 
{
    @Autowired
    private DepartmentFunctionRelationshipsMapper departmentFunctionRelationshipsMapper;

    /**
     * 查询部门职能连接信息
     * 
     * @param id 部门职能连接信息主键
     * @return 部门职能连接信息
     */
    @Override
    public DepartmentFunctionRelationships selectDepartmentFunctionRelationshipsById(Long id)
    {
        return departmentFunctionRelationshipsMapper.selectDepartmentFunctionRelationshipsById(id);
    }

    /**
     * 查询部门职能连接信息列表
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 部门职能连接信息
     */
    @Override
    public List<DepartmentFunctionRelationships> selectDepartmentFunctionRelationshipsList(DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        return departmentFunctionRelationshipsMapper.selectDepartmentFunctionRelationshipsList(departmentFunctionRelationships);
    }

    /**
     * 新增部门职能连接信息
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 结果
     */
    @Override
    public int insertDepartmentFunctionRelationships(DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        return departmentFunctionRelationshipsMapper.insertDepartmentFunctionRelationships(departmentFunctionRelationships);
    }

    /**
     * 修改部门职能连接信息
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 结果
     */
    @Override
    public int updateDepartmentFunctionRelationships(DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        return departmentFunctionRelationshipsMapper.updateDepartmentFunctionRelationships(departmentFunctionRelationships);
    }

    /**
     * 批量删除部门职能连接信息
     * 
     * @param ids 需要删除的部门职能连接信息主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentFunctionRelationshipsByIds(Long[] ids)
    {
        return departmentFunctionRelationshipsMapper.deleteDepartmentFunctionRelationshipsByIds(ids);
    }

    /**
     * 删除部门职能连接信息信息
     * 
     * @param id 部门职能连接信息主键
     * @return 结果
     */
    @Override
    public int deleteDepartmentFunctionRelationshipsById(Long id)
    {
        return departmentFunctionRelationshipsMapper.deleteDepartmentFunctionRelationshipsById(id);
    }
}
