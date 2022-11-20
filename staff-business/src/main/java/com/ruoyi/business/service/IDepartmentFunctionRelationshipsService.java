package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.DepartmentFunctionRelationships;

/**
 * 部门职能连接信息Service接口
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
public interface IDepartmentFunctionRelationshipsService 
{
    /**
     * 查询部门职能连接信息
     * 
     * @param id 部门职能连接信息主键
     * @return 部门职能连接信息
     */
    public DepartmentFunctionRelationships selectDepartmentFunctionRelationshipsById(Long id);

    /**
     * 查询部门职能连接信息列表
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 部门职能连接信息集合
     */
    public List<DepartmentFunctionRelationships> selectDepartmentFunctionRelationshipsList(DepartmentFunctionRelationships departmentFunctionRelationships);

    /**
     * 新增部门职能连接信息
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 结果
     */
    public int insertDepartmentFunctionRelationships(DepartmentFunctionRelationships departmentFunctionRelationships);

    /**
     * 修改部门职能连接信息
     * 
     * @param departmentFunctionRelationships 部门职能连接信息
     * @return 结果
     */
    public int updateDepartmentFunctionRelationships(DepartmentFunctionRelationships departmentFunctionRelationships);

    /**
     * 批量删除部门职能连接信息
     * 
     * @param ids 需要删除的部门职能连接信息主键集合
     * @return 结果
     */
    public int deleteDepartmentFunctionRelationshipsByIds(Long[] ids);

    /**
     * 删除部门职能连接信息信息
     * 
     * @param id 部门职能连接信息主键
     * @return 结果
     */
    public int deleteDepartmentFunctionRelationshipsById(Long id);
}
