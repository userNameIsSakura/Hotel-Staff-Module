package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BasePositionMapper;
import com.ruoyi.business.domain.BasePosition;
import com.ruoyi.business.service.IBasePositionService;

/**
 * 职位信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
@Service
public class BasePositionServiceImpl implements IBasePositionService 
{
    @Autowired
    private BasePositionMapper basePositionMapper;

    /**
     * 查询职位信息
     * 
     * @param positionId 职位信息主键
     * @return 职位信息
     */
    @Override
    public BasePosition selectBasePositionByPositionId(Long positionId)
    {
        return basePositionMapper.selectBasePositionByPositionId(positionId);
    }

    /**
     * 查询职位信息列表
     * 
     * @param basePosition 职位信息
     * @return 职位信息
     */
    @Override
    public List<BasePosition> selectBasePositionList(BasePosition basePosition)
    {
        return basePositionMapper.selectBasePositionList(basePosition);
    }

    /**
     * 新增职位信息
     * 
     * @param basePosition 职位信息
     * @return 结果
     */
    @Override
    public int insertBasePosition(BasePosition basePosition)
    {
        return basePositionMapper.insertBasePosition(basePosition);
    }

    /**
     * 修改职位信息
     * 
     * @param basePosition 职位信息
     * @return 结果
     */
    @Override
    public int updateBasePosition(BasePosition basePosition)
    {
        return basePositionMapper.updateBasePosition(basePosition);
    }

    /**
     * 批量删除职位信息
     * 
     * @param positionIds 需要删除的职位信息主键
     * @return 结果
     */
    @Override
    public int deleteBasePositionByPositionIds(Long[] positionIds)
    {
        return basePositionMapper.deleteBasePositionByPositionIds(positionIds);
    }

    /**
     * 删除职位信息信息
     * 
     * @param positionId 职位信息主键
     * @return 结果
     */
    @Override
    public int deleteBasePositionByPositionId(Long positionId)
    {
        return basePositionMapper.deleteBasePositionByPositionId(positionId);
    }
}
