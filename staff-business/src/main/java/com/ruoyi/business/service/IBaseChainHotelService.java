package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseChainHotel;

/**
 * 连锁酒店Service接口
 *
 * @author ruoyi
 * @date 2023-03-13
 */
public interface IBaseChainHotelService
{
    /**
     * 查询连锁酒店
     *
     * @param chotelId 连锁酒店主键
     * @return 连锁酒店
     */
    public BaseChainHotel selectBaseChainHotelByChotelId(Long chotelId);

    /**
     * 查询连锁酒店
     *
     * @param hotelId chotel id
     * @return {@link BaseChainHotel}
     */
    public Long selectBaseChainHotelByHotelId(Long hotelId);

    /**
     * 查询连锁酒店列表
     *
     * @param baseChainHotel 连锁酒店
     * @return 连锁酒店集合
     */
    public List<BaseChainHotel> selectBaseChainHotelList(BaseChainHotel baseChainHotel);

    /**
     * 新增连锁酒店
     *
     * @param baseChainHotel 连锁酒店
     * @return 结果
     */
    public int insertBaseChainHotel(BaseChainHotel baseChainHotel);

    /**
     * 修改连锁酒店
     *
     * @param baseChainHotel 连锁酒店
     * @return 结果
     */
    public int updateBaseChainHotel(BaseChainHotel baseChainHotel);

    /**
     * 批量删除连锁酒店
     *
     * @param chotelIds 需要删除的连锁酒店主键集合
     * @return 结果
     */
    public int deleteBaseChainHotelByChotelIds(Long[] chotelIds);

    /**
     * 删除连锁酒店信息
     *
     * @param chotelId 连锁酒店主键
     * @return 结果
     */
    public int deleteBaseChainHotelByChotelId(Long chotelId);
}
