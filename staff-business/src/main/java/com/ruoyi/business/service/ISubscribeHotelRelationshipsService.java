package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SubscribeHotelRelationships;

/**
 * 鉴权信息Service接口
 *
 * @author ruoyi
 * @date 2023-01-30
 */
public interface ISubscribeHotelRelationshipsService
{

    public List<SubscribeHotelRelationships> selectSubscribeHotelRelationshipsByContent(String command);

    /**
     * 查询鉴权信息
     *
     * @param id 鉴权信息主键
     * @return 鉴权信息
     */
    public SubscribeHotelRelationships selectSubscribeHotelRelationshipsById(Long id);

    /**
     * 查询鉴权信息列表
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 鉴权信息集合
     */
    public List<SubscribeHotelRelationships> selectSubscribeHotelRelationshipsList(SubscribeHotelRelationships subscribeHotelRelationships);

    /**
     * 新增鉴权信息
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 结果
     */
    public int insertSubscribeHotelRelationships(SubscribeHotelRelationships subscribeHotelRelationships);

    /**
     * 修改鉴权信息
     *
     * @param subscribeHotelRelationships 鉴权信息
     * @return 结果
     */
    public int updateSubscribeHotelRelationships(SubscribeHotelRelationships subscribeHotelRelationships);

    /**
     * 批量删除鉴权信息
     *
     * @param ids 需要删除的鉴权信息主键集合
     * @return 结果
     */
    public int deleteSubscribeHotelRelationshipsByIds(Long[] ids);

    /**
     * 删除鉴权信息信息
     *
     * @param id 鉴权信息主键
     * @return 结果
     */
    public int deleteSubscribeHotelRelationshipsById(Long id);
}
