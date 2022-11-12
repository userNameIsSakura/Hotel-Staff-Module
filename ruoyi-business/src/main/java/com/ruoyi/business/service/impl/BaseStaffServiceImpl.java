package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseStaffMapper;
import com.ruoyi.business.domain.BaseStaff;
import com.ruoyi.business.service.IBaseStaffService;

/**
 * 员工信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-10
 */
@Service
public class BaseStaffServiceImpl implements IBaseStaffService 
{
    @Autowired
    private BaseStaffMapper baseStaffMapper;

    /**
     * 查询员工信息
     * 
     * @param staffId 员工信息主键
     * @return 员工信息
     */
    @Override
    public BaseStaff selectBaseStaffByStaffId(Long staffId)
    {
        return baseStaffMapper.selectBaseStaffByStaffId(staffId);
    }

    /**
     * 查询员工信息列表
     * 
     * @param baseStaff 员工信息
     * @return 员工信息
     */
    @Override
    public List<BaseStaff> selectBaseStaffList(BaseStaff baseStaff)
    {
        return baseStaffMapper.selectBaseStaffList(baseStaff);
    }

    /**
     * 新增员工信息
     * 
     * @param baseStaff 员工信息
     * @return 结果
     */
    @Override
    public int insertBaseStaff(BaseStaff baseStaff)
    {
        return baseStaffMapper.insertBaseStaff(baseStaff);
    }

    /**
     * 修改员工信息
     * 
     * @param baseStaff 员工信息
     * @return 结果
     */
    @Override
    public int updateBaseStaff(BaseStaff baseStaff)
    {
        return baseStaffMapper.updateBaseStaff(baseStaff);
    }

    /**
     * 批量删除员工信息
     * 
     * @param staffIds 需要删除的员工信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseStaffByStaffIds(Long[] staffIds)
    {
        return baseStaffMapper.deleteBaseStaffByStaffIds(staffIds);
    }

    /**
     * 删除员工信息信息
     * 
     * @param staffId 员工信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseStaffByStaffId(Long staffId)
    {
        return baseStaffMapper.deleteBaseStaffByStaffId(staffId);
    }
}
