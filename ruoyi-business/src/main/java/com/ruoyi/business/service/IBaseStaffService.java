package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseStaff;

/**
 * 员工信息Service接口
 * 
 * @author ruoyi
 * @date 2022-11-10
 */
public interface IBaseStaffService 
{
    /**
     * 查询员工信息
     * 
     * @param staffId 员工信息主键
     * @return 员工信息
     */
    public BaseStaff selectBaseStaffByStaffId(Long staffId);

    /**
     * 查询员工信息列表
     * 
     * @param baseStaff 员工信息
     * @return 员工信息集合
     */
    public List<BaseStaff> selectBaseStaffList(BaseStaff baseStaff);

    /**
     * 新增员工信息
     * 
     * @param baseStaff 员工信息
     * @return 结果
     */
    public int insertBaseStaff(BaseStaff baseStaff);

    /**
     * 修改员工信息
     * 
     * @param baseStaff 员工信息
     * @return 结果
     */
    public int updateBaseStaff(BaseStaff baseStaff);

    /**
     * 批量删除员工信息
     * 
     * @param staffIds 需要删除的员工信息主键集合
     * @return 结果
     */
    public int deleteBaseStaffByStaffIds(Long[] staffIds);

    /**
     * 删除员工信息信息
     * 
     * @param staffId 员工信息主键
     * @return 结果
     */
    public int deleteBaseStaffByStaffId(Long staffId);
}
