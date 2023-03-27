package com.ruoyi.business.service.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.HotelDisgramRelationshipsMapper;
import com.ruoyi.business.domain.HotelDisgramRelationships;
import com.ruoyi.business.service.IHotelDisgramRelationshipsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 酒店介绍图Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-14
 */
@Service
public class HotelDisgramRelationshipsServiceImpl implements IHotelDisgramRelationshipsService
{
    @Autowired
    private HotelDisgramRelationshipsMapper hotelDisgramRelationshipsMapper;

    /**
     * 查询酒店介绍图
     *
     * @param id 酒店介绍图主键
     * @return 酒店介绍图
     */
    @Override
    public HotelDisgramRelationships selectHotelDisgramRelationshipsById(Long id)
    {
        return hotelDisgramRelationshipsMapper.selectHotelDisgramRelationshipsById(id);
    }

    /**
     * 查询酒店介绍图列表
     *
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 酒店介绍图
     */
    @Override
    public List<HotelDisgramRelationships> selectHotelDisgramRelationshipsList(HotelDisgramRelationships hotelDisgramRelationships)
    {
        return hotelDisgramRelationshipsMapper.selectHotelDisgramRelationshipsList(hotelDisgramRelationships);
    }

    /**
     * 新增酒店介绍图
     *
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 结果
     */
    @Override
    @Transactional
    public int insertHotelDisgramRelationships(HotelDisgramRelationships hotelDisgramRelationships)
    {
        final HotelDisgramRelationships r = new HotelDisgramRelationships();
        r.setDiagramType(hotelDisgramRelationships.getDiagramType());
        r.setHotelId(SecurityUtils.getHotelId());
        final List<HotelDisgramRelationships> list = selectHotelDisgramRelationshipsList(r);
        if(list.size() > 0) {
            final HotelDisgramRelationships newDiagram = list.get(0);
            newDiagram.setDiagramPath(newDiagram.getDiagramPath()+","+hotelDisgramRelationships.getDiagramPath());
            return hotelDisgramRelationshipsMapper.updateHotelDisgramRelationships(newDiagram);
        }
        return hotelDisgramRelationshipsMapper.insertHotelDisgramRelationships(hotelDisgramRelationships);
    }

    /**
     * 修改酒店介绍图
     *
     * @param hotelDisgramRelationships 酒店介绍图
     * @return 结果
     */
    @Override
    @Transactional
    public int updateHotelDisgramRelationships(HotelDisgramRelationships hotelDisgramRelationships)
    {
        final HotelDisgramRelationships r = new HotelDisgramRelationships();
        r.setDiagramType(hotelDisgramRelationships.getDiagramType());
        r.setHotelId(SecurityUtils.getHotelId());
        final List<HotelDisgramRelationships> list = selectHotelDisgramRelationshipsList(r);
        if(list.size() > 0 && !list.get(0).getId().equals(hotelDisgramRelationships.getId())) {
            final HotelDisgramRelationships newDiagram = list.get(0);
            newDiagram.setDiagramPath(newDiagram.getDiagramPath()+","+hotelDisgramRelationships.getDiagramPath());
            hotelDisgramRelationshipsMapper.updateHotelDisgramRelationships(newDiagram);
            return hotelDisgramRelationshipsMapper.deleteHotelDisgramRelationshipsById(hotelDisgramRelationships.getId());
        }
        return hotelDisgramRelationshipsMapper.updateHotelDisgramRelationships(hotelDisgramRelationships);
    }

    /**
     * 批量删除酒店介绍图
     *
     * @param ids 需要删除的酒店介绍图主键
     * @return 结果
     */
    @Override
    public int deleteHotelDisgramRelationshipsByIds(Long[] ids)
    {
        return hotelDisgramRelationshipsMapper.deleteHotelDisgramRelationshipsByIds(ids);
    }

    /**
     * 删除酒店介绍图信息
     *
     * @param id 酒店介绍图主键
     * @return 结果
     */
    @Override
    public int deleteHotelDisgramRelationshipsById(Long id)
    {
        return hotelDisgramRelationshipsMapper.deleteHotelDisgramRelationshipsById(id);
    }
}
