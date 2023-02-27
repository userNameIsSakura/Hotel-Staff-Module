package com.ruoyi.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseHotelMapper;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.IBaseHotelService;

/**
 * 酒店列表Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-06
 */
@Service
public class BaseHotelServiceImpl implements IBaseHotelService
{
    @Autowired
    private BaseHotelMapper baseHotelMapper;


    /**
     * 查询酒店列表
     *
     * @param hotelId 酒店列表主键
     * @return 酒店列表
     */
    @Override
    public BaseHotel selectBaseHotelByHotelId(Long hotelId)
    {
        BaseHotel baseHotel = baseHotelMapper.selectBaseHotelByHotelId(hotelId);
        return baseHotel;
    }

    /**
     * 查询酒店列表列表
     *
     * @param baseHotel 酒店列表
     * @return 酒店列表
     */
    @Override
    public List<BaseHotel> selectBaseHotelList(BaseHotel baseHotel)
    {
        List<BaseHotel> baseHotels = baseHotelMapper.selectBaseHotelList(baseHotel);
        return baseHotels;
    }

    /**
     * 新增酒店列表
     *
     * @param baseHotel 酒店列表
     * @return 结果
     */
    @Override
    public int insertBaseHotel(BaseHotel baseHotel)
    {
        String prefix = baseHotel.getHotelNumber().substring(0, 6);
        String groupId = baseHotel.getHotelNumber().substring(6, 8);

        String num = baseHotelMapper.selectNumByAddressPrefix(prefix);

        if( num == null ) {
            num = "00";
        }else {
            num = num.substring(8,10);
        }

        int newId = Integer.parseInt(num) + 1;

        String id;
        if(newId < 10) {
            id = "0" + newId;
        }else {
            id = String.valueOf(newId);
        }

        baseHotel.setHotelNumber(prefix + groupId + id);
        int i = baseHotelMapper.insertBaseHotel(baseHotel);

        return i;
    }

    /**
     * 修改酒店列表
     *
     * @param baseHotel 酒店列表
     * @return 结果
     */
    @Override
    public int updateBaseHotel(BaseHotel baseHotel)
    {

        String prefix = baseHotel.getHotelNumber().substring(0, 6);
        String groupId = baseHotel.getHotelNumber().substring(6, 8);

        String num = baseHotelMapper.selectNumByAddressPrefix(prefix);

        if( num == null ) {
            num = "00";
        }else {
            num = num.substring(8,10);
        }

        int newId = Integer.parseInt(num) + 1;

        String id;
        if(newId < 10) {
            id = "0" + newId;
        }else {
            id = String.valueOf(newId);
        }

        baseHotel.setHotelNumber(prefix + groupId + id);

        return baseHotelMapper.updateBaseHotel(baseHotel);
    }

    /**
     * 批量删除酒店列表
     *
     * @param hotelIds 需要删除的酒店列表主键
     * @return 结果
     */
    @Override
    public int deleteBaseHotelByHotelIds(Long[] hotelIds)
    {
        return baseHotelMapper.deleteBaseHotelByHotelIds(hotelIds);
    }

    /**
     * 删除酒店列表信息
     *
     * @param hotelId 酒店列表主键
     * @return 结果
     */
    @Override
    public int deleteBaseHotelByHotelId(Long hotelId)
    {
        return baseHotelMapper.deleteBaseHotelByHotelId(hotelId);
    }
}
