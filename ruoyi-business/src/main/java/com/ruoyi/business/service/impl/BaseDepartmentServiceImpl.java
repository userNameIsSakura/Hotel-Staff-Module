package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.business.domain.*;
import com.ruoyi.business.mapper.BaseFunctionMapper;
import com.ruoyi.business.mapper.BasePositionMapper;
import com.ruoyi.business.mapper.DepartmentFunctionRelationshipsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.mapper.BaseDepartmentMapper;
import com.ruoyi.business.service.IBaseDepartmentService;

/**
 * 部门信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-12
 */
@Service
public class BaseDepartmentServiceImpl implements IBaseDepartmentService
{
    @Autowired
    private BaseDepartmentMapper baseDepartmentMapper;
    @Autowired
    private BaseFunctionMapper baseFunctionMapper;
    @Autowired
    private DepartmentFunctionRelationshipsMapper departmentFunctionRelationshipsMapper;

    /**
     * 查询部门信息
     *
     * @param departmentId 部门信息主键
     * @return 部门信息
     */
    @Override
    public BaseDepartment selectBaseDepartmentByDepartmentId(Long departmentId)
    {

        BaseDepartment baseDepartment = baseDepartmentMapper.selectBaseDepartmentByDepartmentId(departmentId);
        return baseDepartment;
    }

    /**
     * 查询部门信息列表
     *
     * @param baseDepartment 部门信息
     * @return 部门信息
     */
    @Override
    public List<BaseDepartment> selectBaseDepartmentList(BaseDepartment baseDepartment)
    {
        return baseDepartmentMapper.selectBaseDepartmentList(baseDepartment);
    }

    /**
     * 新增部门信息
     *
     * @param baseDepartment 部门信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertBaseDepartment(BaseDepartment baseDepartment)
    {
        int rows = baseDepartmentMapper.insertBaseDepartment(baseDepartment);
        insertBaseFunction(baseDepartment);
        return rows;
    }

    /**
     * 修改部门信息
     *
     * @param baseDepartment 部门信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateBaseDepartment(BaseDepartment baseDepartment)
    {
        baseDepartmentMapper.deleteBaseFunctionByDepartmentId(baseDepartment.getDepartmentId());
        insertBaseFunction(baseDepartment);

        return baseDepartmentMapper.updateBaseDepartment(baseDepartment);
    }

    /**
     * 批量删除部门信息
     *
     * @param departmentIds 需要删除的部门信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBaseDepartmentByDepartmentIds(Long[] departmentIds)
    {
        baseDepartmentMapper.deleteBaseStaffByDepartmentIds(departmentIds);
        return baseDepartmentMapper.deleteBaseDepartmentByDepartmentIds(departmentIds);
    }

    /**
     * 删除部门信息信息
     *
     * @param departmentId 部门信息主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteBaseDepartmentByDepartmentId(Long departmentId)
    {
        baseDepartmentMapper.deleteBaseStaffByDepartmentId(departmentId);
        return baseDepartmentMapper.deleteBaseDepartmentByDepartmentId(departmentId);
    }

    /**
     * 新增职能信息信息
     *
     * @param baseDepartment 部门信息对象
     */
    public void insertBaseStaff(BaseDepartment baseDepartment)
    {
        List<BaseStaff> baseStaffList = baseDepartment.getBaseStaffList();
        Long departmentId = baseDepartment.getDepartmentId();
        if (StringUtils.isNotNull(baseStaffList))
        {
            List<BaseStaff> list = new ArrayList<BaseStaff>();
            for (BaseStaff baseStaff : baseStaffList)
            {
                baseStaff.setDepartmentId(departmentId);
                list.add(baseStaff);
            }
            if (list.size() > 0)
            {
                baseDepartmentMapper.batchBaseStaff(list);
            }
        }
    }


    /**
     *
     * 新增职能信息
     *
     * */
    public void insertBaseFunction(BaseDepartment baseDepartment)
    {
        List<BasePosition> basePositionList = baseDepartment.getBasePositionList();
        if (StringUtils.isNotNull(basePositionList))
        {
            basePositionList.forEach(p -> {
                BaseFunction f = new BaseFunction();
                f.setFunctionValue(p.getFunctionValue());
                f.setRemark(p.getRemark());

                baseFunctionMapper.insertBaseFunction(f);

                DepartmentFunctionRelationships departmentFunctionRelationships = new DepartmentFunctionRelationships();
                departmentFunctionRelationships.setFunctionId(f.getFunctionId());
                departmentFunctionRelationships.setDepartmentId(baseDepartment.getDepartmentId());
                departmentFunctionRelationships.setPositionId(p.getPositionId());
                departmentFunctionRelationshipsMapper.insertDepartmentFunctionRelationships(departmentFunctionRelationships);

            });
        }
    }


}
