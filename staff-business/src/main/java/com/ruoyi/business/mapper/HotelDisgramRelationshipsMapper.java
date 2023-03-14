package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.HotelDisgramRelationships;

/**
 * 酒店介绍图Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-14
 */
public interface HotelDisgramRelationshipsMapper 
{
    /**
     * 查询酒店介绍图
     * 
     * @param id 酒店介绍图主键
     * @return 酒店介绍图
     */
    public HotelDisgramRelationships selectHotelDisgramRelationshipsById(Long id);

    /**
     * 查询酒店介绍图列表
     * 
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 酒店介绍图集合
     */
    public List<HotelDisgramRelationships> selectHotelDisgramRelationshipsList(HotelDisgramRelationships hotelDisgramRelationships);

    /**
     * 新增酒店介绍图
     * 
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 结果
     */
    public int insertHotelDisgramRelationships(HotelDisgramRelationships hotelDisgramRelationships);

    /**
     * 修改酒店介绍图
     * 
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 结果
     */
    public int updateHotelDisgramRelationships(HotelDisgramRelationships hotelDisgramRelationships);

    /**
     * 删除酒店介绍图
     * 
     * @param id 酒店介绍图主键
     * @return 结果
     */
    public int deleteHotelDisgramRelationshipsById(Long id);

    /**
     * 批量删除酒店介绍图
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHotelDisgramRelationshipsByIds(Long[] ids);
}
