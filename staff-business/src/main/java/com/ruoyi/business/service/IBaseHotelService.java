package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseHotel;

/**
 * 酒店列表Service接口
 *
 * @author ruoyi
 * @date 2023-01-06
 */
public interface IBaseHotelService
{
    /**
     * 查询酒店列表
     *
     * @param hotelId 酒店列表主键
     * @return 酒店列表
     */
    public BaseHotel selectBaseHotelByHotelId(Long hotelId);

    public List<BaseHotel> selectBaseHotelByArea(String acode,String hotelName);

    /**
     * 查询酒店列表
     *
     * @param chotelId 酒店列表主键
     * @return 酒店列表
     */
    public BaseHotel selectBaseHotelByChotelId(Long chotelId);

    /**
     * 查询酒店列表列表
     *
     * @param baseHotel 酒店列表
     * @return 酒店列表集合
     */
    public List<BaseHotel> selectBaseHotelList(BaseHotel baseHotel);

    /**
     * 新增酒店列表
     *
     * @param baseHotel 酒店列表
     * @return 结果
     */
    public int insertBaseHotel(BaseHotel baseHotel);

    /**
     * 修改酒店列表
     *
     * @param baseHotel 酒店列表
     * @return 结果
     */
    public int updateBaseHotel(BaseHotel baseHotel);

    /**
     * 批量删除酒店列表
     *
     * @param hotelIds 需要删除的酒店列表主键集合
     * @return 结果
     */
    public int deleteBaseHotelByHotelIds(Long[] hotelIds);

    /**
     * 删除酒店列表信息
     *
     * @param hotelId 酒店列表主键
     * @return 结果
     */
    public int deleteBaseHotelByHotelId(Long hotelId);
}
