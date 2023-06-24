package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.service.impl.BaseChainHotelServiceImpl;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;

@RestController
@RequestMapping("/business/aosMember")
public class AOSMemberController {

    @Value("${hotel.member.url}")
    private String memberUrl;
    @Autowired
    private BaseChainHotelServiceImpl baseChainHotelService;
    @Autowired
    private BaseHotelServiceImpl baseHotelService;

    /* 酒店集团注册到会员系统 */
    @GetMapping("/memberRegister")
    public Object register() throws IOException {
        final BaseChainHotel baseChainHotel = baseChainHotelService.selectBaseChainHotelByChotelId(SecurityUtils.getHotelId());
        if(baseChainHotel.getChotelParent() != 0) {
            return AjaxResult.error("仅集团管理员可以操作加入会员系统");
        }
        final HashMap<String, Object> map = new HashMap<>();
        map.put("hotelId",baseChainHotel.getChotelId());
        map.put("hotelName",baseChainHotel.getChotelName());
        return HttpUtils.sendPost2(memberUrl + "system/hotel/register", JSONObject.toJSONString(map));
    }

    /* 查询当前用户的酒店是否注册到会员系统 */
    @GetMapping("/validation")
    public boolean validation() throws IOException {
        if(SecurityUtils.getHotelId() == null)
            return false;
        final BaseChainHotel baseChainHotel = baseChainHotelService.selectBaseChainHotelByChotelId(SecurityUtils.getHotelId());
        Long cHotelId;
        if(baseChainHotel.getChotelParent() == 0) {
            cHotelId = baseChainHotel.getChotelId();
        }else {
            cHotelId = baseChainHotel.getChotelParent();
        }
        return !StringUtils.isEmpty(HttpUtils.sendGet(memberUrl + "system/hotel/" + cHotelId));
    }

}
