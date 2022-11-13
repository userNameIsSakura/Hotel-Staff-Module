package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BaseDepartment;
import com.ruoyi.business.domain.BaseStaff;

/**
 * 部门信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-12
 */
public interface BaseDepartmentMapper
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
     * 删除部门信息
     *
     * @param departmentId 部门信息主键
     * @return 结果
     */
    public int deleteBaseDepartmentByDepartmentId(Long departmentId);

    /**
     * 批量删除部门信息
     *
     * @param departmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseDepartmentByDepartmentIds(Long[] departmentIds);

    /**
     * 批量删除员工信息
     *
     * @param departmentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseStaffByDepartmentIds(Long[] departmentIds);

    /**
     * 批量新增员工信息
     *
     * @param baseStaffList 员工信息列表
     * @return 结果
     */
    public int batchBaseStaff(List<BaseStaff> baseStaffList);


    /**
     * 通过部门信息主键删除员工信息信息
     *
     * @param departmentId 部门信息ID
     * @return 结果
     */
    public int deleteBaseStaffByDepartmentId(Long departmentId);
}
