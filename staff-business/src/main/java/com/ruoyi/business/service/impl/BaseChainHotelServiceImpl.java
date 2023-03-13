package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseChainHotelMapper;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.service.IBaseChainHotelService;

/**
 * 连锁酒店Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-13
 */
@Service
public class BaseChainHotelServiceImpl implements IBaseChainHotelService 
{
    @Autowired
    private BaseChainHotelMapper baseChainHotelMapper;

    /**
     * 查询连锁酒店
     * 
     * @param chotelId 连锁酒店主键
     * @return 连锁酒店
     */
    @Override
    public BaseChainHotel selectBaseChainHotelByChotelId(Long chotelId)
    {
        return baseChainHotelMapper.selectBaseChainHotelByChotelId(chotelId);
    }

    /**
     * 查询连锁酒店列表
     * 
     * @param baseChainHotel 连锁酒店
     * @return 连锁酒店
     */
    @Override
    public List<BaseChainHotel> selectBaseChainHotelList(BaseChainHotel baseChainHotel)
    {
        return baseChainHotelMapper.selectBaseChainHotelList(baseChainHotel);
    }

    /**
     * 新增连锁酒店
     * 
     * @param baseChainHotel 连锁酒店
     * @return 结果
     */
    @Override
    public int insertBaseChainHotel(BaseChainHotel baseChainHotel)
    {
        return baseChainHotelMapper.insertBaseChainHotel(baseChainHotel);
    }

    /**
     * 修改连锁酒店
     * 
     * @param baseChainHotel 连锁酒店
     * @return 结果
     */
    @Override
    public int updateBaseChainHotel(BaseChainHotel baseChainHotel)
    {
        return baseChainHotelMapper.updateBaseChainHotel(baseChainHotel);
    }

    /**
     * 批量删除连锁酒店
     * 
     * @param chotelIds 需要删除的连锁酒店主键
     * @return 结果
     */
    @Override
    public int deleteBaseChainHotelByChotelIds(Long[] chotelIds)
    {
        return baseChainHotelMapper.deleteBaseChainHotelByChotelIds(chotelIds);
    }

    /**
     * 删除连锁酒店信息
     * 
     * @param chotelId 连锁酒店主键
     * @return 结果
     */
    @Override
    public int deleteBaseChainHotelByChotelId(Long chotelId)
    {
        return baseChainHotelMapper.deleteBaseChainHotelByChotelId(chotelId);
    }
}
