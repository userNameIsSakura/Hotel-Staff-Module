package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.service.IBaseChainHotelService;

import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/business/member")
public class MemberController {

    @Autowired
    private IBaseChainHotelService baseChainHotelService;


    /**
     * 验证酒店名是否合法
     * */
    @PostMapping("/verifyHotelName")
    public Long verifyHostName(@RequestParam(value = "hotelName") String hotelName) {
        final BaseChainHotel baseChainHotel = new BaseChainHotel();
        baseChainHotel.setChotelName(hotelName);
        baseChainHotel.setChotelParent(0L);
        final List<BaseChainHotel> baseChainHotels = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);
        return baseChainHotels.size() == 0 ? 0 : baseChainHotels.get(0).getChotelId();
    }



}
