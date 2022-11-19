package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BaseStaff;

/**
 * 员工信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-10
 */
public interface BaseStaffMapper
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
     * 删除员工信息
     *
     * @param staffId 员工信息主键
     * @return 结果
     */
    public int deleteBaseStaffByStaffId(Long staffId);

    /**
     * 批量删除员工信息
     *
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseStaffByStaffIds(Long[] staffIds);

    /**
     *
     * 根据staffId删除职位-员工关联信息
     *
     * */
    public int deleteSRPByStaffId(Long staffId);
}
