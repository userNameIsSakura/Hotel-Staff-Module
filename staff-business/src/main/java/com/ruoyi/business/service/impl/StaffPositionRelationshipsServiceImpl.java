package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.StaffPositionRelationshipsMapper;
import com.ruoyi.business.domain.StaffPositionRelationships;
import com.ruoyi.business.service.IStaffPositionRelationshipsService;

/**
 * 员工职位关联信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-19
 */
@Service
public class StaffPositionRelationshipsServiceImpl implements IStaffPositionRelationshipsService 
{
    @Autowired
    private StaffPositionRelationshipsMapper staffPositionRelationshipsMapper;

    /**
     * 查询员工职位关联信息
     * 
     * @param id 员工职位关联信息主键
     * @return 员工职位关联信息
     */
    @Override
    public StaffPositionRelationships selectStaffPositionRelationshipsById(Long id)
    {
        return staffPositionRelationshipsMapper.selectStaffPositionRelationshipsById(id);
    }

    /**
     * 查询员工职位关联信息列表
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 员工职位关联信息
     */
    @Override
    public List<StaffPositionRelationships> selectStaffPositionRelationshipsList(StaffPositionRelationships staffPositionRelationships)
    {
        return staffPositionRelationshipsMapper.selectStaffPositionRelationshipsList(staffPositionRelationships);
    }

    /**
     * 新增员工职位关联信息
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 结果
     */
    @Override
    public int insertStaffPositionRelationships(StaffPositionRelationships staffPositionRelationships)
    {
        return staffPositionRelationshipsMapper.insertStaffPositionRelationships(staffPositionRelationships);
    }

    /**
     * 修改员工职位关联信息
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 结果
     */
    @Override
    public int updateStaffPositionRelationships(StaffPositionRelationships staffPositionRelationships)
    {
        return staffPositionRelationshipsMapper.updateStaffPositionRelationships(staffPositionRelationships);
    }

    /**
     * 批量删除员工职位关联信息
     * 
     * @param ids 需要删除的员工职位关联信息主键
     * @return 结果
     */
    @Override
    public int deleteStaffPositionRelationshipsByIds(Long[] ids)
    {
        return staffPositionRelationshipsMapper.deleteStaffPositionRelationshipsByIds(ids);
    }

    /**
     * 删除员工职位关联信息信息
     * 
     * @param id 员工职位关联信息主键
     * @return 结果
     */
    @Override
    public int deleteStaffPositionRelationshipsById(Long id)
    {
        return staffPositionRelationshipsMapper.deleteStaffPositionRelationshipsById(id);
    }
}
