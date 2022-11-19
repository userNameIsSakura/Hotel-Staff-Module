package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.StaffPositionRelationships;

/**
 * 员工职位关联信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-19
 */
public interface StaffPositionRelationshipsMapper 
{
    /**
     * 查询员工职位关联信息
     * 
     * @param id 员工职位关联信息主键
     * @return 员工职位关联信息
     */
    public StaffPositionRelationships selectStaffPositionRelationshipsById(Long id);

    /**
     * 查询员工职位关联信息列表
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 员工职位关联信息集合
     */
    public List<StaffPositionRelationships> selectStaffPositionRelationshipsList(StaffPositionRelationships staffPositionRelationships);

    /**
     * 新增员工职位关联信息
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 结果
     */
    public int insertStaffPositionRelationships(StaffPositionRelationships staffPositionRelationships);

    /**
     * 修改员工职位关联信息
     * 
     * @param staffPositionRelationships 员工职位关联信息
     * @return 结果
     */
    public int updateStaffPositionRelationships(StaffPositionRelationships staffPositionRelationships);

    /**
     * 删除员工职位关联信息
     * 
     * @param id 员工职位关联信息主键
     * @return 结果
     */
    public int deleteStaffPositionRelationshipsById(Long id);

    /**
     * 批量删除员工职位关联信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStaffPositionRelationshipsByIds(Long[] ids);
}
