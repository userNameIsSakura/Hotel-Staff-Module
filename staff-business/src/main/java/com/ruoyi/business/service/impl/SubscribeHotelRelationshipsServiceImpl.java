package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.SubscribeHotelRelationshipsMapper;
import com.ruoyi.business.domain.SubscribeHotelRelationships;
import com.ruoyi.business.service.ISubscribeHotelRelationshipsService;

/**
 * 鉴权信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-30
 */
@Service
public class SubscribeHotelRelationshipsServiceImpl implements ISubscribeHotelRelationshipsService
{
    @Autowired
    private SubscribeHotelRelationshipsMapper subscribeHotelRelationshipsMapper;

    @Override
    public List<SubscribeHotelRelationships> selectSubscribeHotelRelationshipsByContent(String command) {
        return subscribeHotelRelationshipsMapper.selectSubscribeHotelRelationshipsByContent(command);
    }

    /**
     * 查询鉴权信息
     *
     * @param id 鉴权信息主键
     * @return 鉴权信息
     */
    @Override
    public SubscribeHotelRelationships selectSubscribeHotelRelationshipsById(Long id)
    {
        return subscribeHotelRelationshipsMapper.selectSubscribeHotelRelationshipsById(id);
    }

    /**
     * 查询鉴权信息列表
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 鉴权信息
     */
    @Override
    public List<SubscribeHotelRelationships> selectSubscribeHotelRelationshipsList(SubscribeHotelRelationships subscribeHotelRelationships)
    {
        return subscribeHotelRelationshipsMapper.selectSubscribeHotelRelationshipsList(subscribeHotelRelationships);
    }

    /**
     * 新增鉴权信息
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 结果
     */
    @Override
    public int insertSubscribeHotelRelationships(SubscribeHotelRelationships subscribeHotelRelationships)
    {
        return subscribeHotelRelationshipsMapper.insertSubscribeHotelRelationships(subscribeHotelRelationships);
    }

    /**
     * 修改鉴权信息
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 结果
     */
    @Override
    public int updateSubscribeHotelRelationships(SubscribeHotelRelationships subscribeHotelRelationships)
    {
        return subscribeHotelRelationshipsMapper.updateSubscribeHotelRelationships(subscribeHotelRelationships);
    }

    /**
     * 批量删除鉴权信息
     *
     * @param ids 需要删除的鉴权信息主键
     * @return 结果
     */
    @Override
    public int deleteSubscribeHotelRelationshipsByIds(Long[] ids)
    {
        return subscribeHotelRelationshipsMapper.deleteSubscribeHotelRelationshipsByIds(ids);
    }

    /**
     * 删除鉴权信息信息
     *
     * @param id 鉴权信息主键
     * @return 结果
     */
    @Override
    public int deleteSubscribeHotelRelationshipsById(Long id)
    {
        return subscribeHotelRelationshipsMapper.deleteSubscribeHotelRelationshipsById(id);
    }
}
