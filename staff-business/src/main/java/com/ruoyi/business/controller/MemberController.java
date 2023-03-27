package com.ruoyi.business.controller;

import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
