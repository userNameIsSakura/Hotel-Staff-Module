package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("/business/rule")
public class BaseRuleController {

    @Value("${hotel.member.url}")
    private String memberUrl;

    @Autowired
    private IBaseChainHotelService baseChainHotelService;

    private Long getChainHotelId() {
        if(SecurityUtils.getSuperAdministrator() == 0L) {
            // 集团管理员
            return SecurityUtils.getHotelId();
        }else if(SecurityUtils.getSuperAdministrator() == 1L) {
            // 超级管理员
            return 0L;
        }else {
            // 酒店管理员
            return baseChainHotelService.selectBaseChainHotelByChotelId(SecurityUtils.getHotelId()).getChotelParent();
        }
    }

    /**
     * 查询优惠规则列表
     */
    @PostMapping("/list")
    public Object list(@RequestBody HashMap baseCouponRule) throws IOException {
        try {
            baseCouponRule.put("chotelId",getChainHotelId());
            return HttpUtils.sendPost2(memberUrl + "system/rule/list", JSONObject.toJSONString(baseCouponRule));
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }

    @PostMapping("/issue")
    public Object issue(@RequestBody HashMap baseCouponRule) throws IOException {
        try {
            baseCouponRule.put("chotelId",getChainHotelId());
            return HttpUtils.sendPost2(memberUrl + "system/coupon/issue", JSONObject.toJSONString(baseCouponRule));
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }

    @PostMapping("/sign")
    public Object signRule(@RequestBody HashMap baseCouponRule) throws IOException {
        try {
            baseCouponRule.put("chotelId",getChainHotelId());
            return HttpUtils.sendPost2(memberUrl + "system/rule/sign", JSONObject.toJSONString(baseCouponRule));
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }


    @PostMapping("/addRule")
    public Object addRule(@RequestBody HashMap baseCouponRule) throws IOException {
        try {
            baseCouponRule.put("chotelId",getChainHotelId());
            return HttpUtils.sendPost2(memberUrl + "system/rule", JSONObject.toJSONString(baseCouponRule));
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }

    @PostMapping(value = "/updateRule", produces="application/json;charset=UTF-8")
    public Object updateRule(@RequestBody HashMap baseCouponRule, HttpServletRequest request) throws IOException {
        try {
            baseCouponRule.put("chotelId",getChainHotelId());
            return HttpUtils.sendPost2(memberUrl + "system/rule/update", JSONObject.toJSONString(baseCouponRule));
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }

    @GetMapping("/{ruleId}")
    public Object getRule(@PathVariable Long ruleId) throws IOException {
        try {
            return HttpUtils.sendGet(memberUrl + "system/rule/" + ruleId);
        } catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }

    }

    @GetMapping("/delete/{ruleIds}")
    public Object delete(@PathVariable Long[] ruleIds) throws IOException {
        try {
            final String s = Arrays.toString(ruleIds);
            return HttpUtils.sendGet(memberUrl + "system/rule/delete/" + s.substring(1,s.length()-1) + "/" + getChainHotelId());
        } catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }

    }

    /**
     * 查询优惠规则列表
     */
    @PostMapping("/listSign")
    public Object listSignRule() throws IOException {
        try {
            return HttpUtils.sendPost2(memberUrl + "system/rule/listSign","");
        }catch (Exception e) {
            return AjaxResult.error("会员模块异常,请联系管理员");
        }
    }








}
