package com.ruoyi.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.business.domain.*;
import com.ruoyi.business.mapper.StaffPositionRelationshipsMapper;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseStaffMapper;
import com.ruoyi.business.service.IBaseStaffService;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private StaffPositionRelationshipsMapper staffPositionRelationshipsMapper;


    @Override
    public int checkUrl(Long staffId, String url) {
        return baseStaffMapper.checkUrl(staffId, url);
    }

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
        int n = baseStaffMapper.insertBaseStaff(baseStaff);
        insertSRP(baseStaff);
        /*新增员工角色关联信息*/
        insertStaffRole(baseStaff);
        return n;
    }

    /**
     * 修改员工信息
     *
     * @param baseStaff 员工信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBaseStaff(BaseStaff baseStaff)
    {
        /*删除职位-员工关联信息*/
        baseStaffMapper.deleteSRPByStaffId(baseStaff.getStaffId());
        /*删除员工角色关联信息*/
        baseStaffMapper.deleteStaffRoleByStaffId(baseStaff.getStaffId());
        /*新增员工角色关联信息*/
        insertStaffRole(baseStaff);
        /*新增职位-员工关联信息*/
        insertSRP(baseStaff);
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



    /**
     *
     * 新增员工-职位关联信息
     *
     * */
    public void insertSRP(BaseStaff baseStaff)
    {
        List<BasePosition> basePositionList = baseStaff.getBasePositionList();
        if (StringUtils.isNotNull(basePositionList))
        {
            basePositionList.forEach(p -> {

                StaffPositionRelationships staffPositionRelationships = new StaffPositionRelationships();
                staffPositionRelationships.setStaffId(baseStaff.getStaffId());
                staffPositionRelationships.setPositionId(p.getPositionId());

                staffPositionRelationshipsMapper.insertStaffPositionRelationships(staffPositionRelationships);
            });
        }
    }

    public void insertStaffRole(BaseStaff baseStaff) {
        Long[] roles = baseStaff.getRoles();
        if(roles.length != 0) {
            List<StaffRoleRelationships> list = new ArrayList<>();
            for (Long role : roles) {
                StaffRoleRelationships staffRoleRelationships = new StaffRoleRelationships();
                staffRoleRelationships.setRoleId(role);
                staffRoleRelationships.setStaffId(baseStaff.getStaffId());
                list.add(staffRoleRelationships);
            }
            baseStaffMapper.batchStaffRole(list);
        }
    }


}
