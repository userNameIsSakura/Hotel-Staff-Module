package com.ruoyi.business.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ruoyi.business.domain.BaseHotel;
import org.apache.ibatis.annotations.Param;

/**
 * 酒店列表Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-06
 */
public interface BaseHotelMapper
{
    /**
     * 查询酒店列表
     *
     * @param hotelId 酒店列表主键
     * @return 酒店列表
     */
    public BaseHotel selectBaseHotelByHotelId(Long hotelId);

    public List<BaseHotel> selectBaseHotelByHotelIdArray(ArrayList idList);

    /**
     * 对外查询酒店接口
     *
     * @param
     * */
    public List<BaseHotel> selectBaseHotelForOutside(HashMap<String,Object> map);

    /**
     * 查询酒店列表
     *
     * @param chotelId 酒店列表主键
     * @return 酒店列表
     */
    public BaseHotel selectBaseHotelByChotelId(Long chotelId);

    public List<BaseHotel> selectBaseHotelByArea(@Param(value = "acode") String acode,@Param(value = "hotelName")String hotelName);

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
     * 根据地址前缀查询酒店数量
     *
     * @param prefix 前缀
     * @return int
     */
    public String selectNumByAddressPrefix(String prefix);

    /**
     * 修改酒店列表
     *
     * @param baseHotel 酒店列表
     * @return 结果
     */
    public int updateBaseHotel(BaseHotel baseHotel);

    /**
     * 删除酒店列表
     *
     * @param hotelId 酒店列表主键
     * @return 结果
     */
    public int deleteBaseHotelByHotelId(Long hotelId);

    /**
     * 批量删除酒店列表
     *
     * @param hotelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseHotelByHotelIds(Long[] hotelIds);
}
