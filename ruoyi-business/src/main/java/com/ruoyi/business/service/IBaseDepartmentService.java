package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseDepartment;

/**
 * 部门信息Service接口
 *
 * @author ruoyi
 * @date 2022-11-12
 */
public interface IBaseDepartmentService
{
    /**
     * 查询部门信息
     *
     * @param departmentId 部门信息主键
     * @return 部门信息
     */
    public BaseDepartment selectBaseDepartmentByDepartmentId(Long departmentId);

    /**
     * 查询部门信息列表
     *
     * @param baseDepartment 部门信息
     * @return 部门信息集合
     */
    public List<BaseDepartment> selectBaseDepartmentList(BaseDepartment baseDepartment);

    /**
     * 新增部门信息
     *
     * @param baseDepartment 部门信息
     * @return 结果
     */
    public int insertBaseDepartment(BaseDepartment baseDepartment);

    /**
     * 修改部门信息
     *
     * @param baseDepartment 部门信息
     * @return 结果
     */
    public int updateBaseDepartment(BaseDepartment baseDepartment);

    /**
     * 批量删除部门信息
     *
     * @param departmentIds 需要删除的部门信息主键集合
     * @return 结果
     */
    public int deleteBaseDepartmentByDepartmentIds(Long[] departmentIds);

    /**
     * 删除部门信息信息
     *
     * @param departmentId 部门信息主键
     * @return 结果
     */
    public int deleteBaseDepartmentByDepartmentId(Long departmentId);
}
