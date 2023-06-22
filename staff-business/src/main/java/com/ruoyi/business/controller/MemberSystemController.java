package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.annotations.StaffTokenCheck;
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.impl.BaseChainHotelServiceImpl;
import com.ruoyi.business.service.impl.BaseHotelServiceImpl;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/business/memberSystem")
@StaffTokenCheck
public class MemberSystemController {

    @Value("${hotel.member.url}")
    private String memberUrl;
    @Autowired
    private BaseChainHotelServiceImpl baseChainHotelService;
    @Autowired
    private BaseHotelServiceImpl baseHotelService;
    @Autowired
    private TokenService tokenService;


    @PostMapping(value = "/member/register", produces="application/json;charset=UTF-8")
    public String register(@RequestBody HashMap map, HttpServletRequest request) throws IOException {

        return HttpUtils.sendPost2(memberUrl + "system/member/register", JSONObject.toJSONString(map));
    }

    @PostMapping(value = "/regular/add", produces="application/json;charset=UTF-8")
    public String regularAdd(@RequestBody HashMap map,HttpServletRequest request) throws  IOException {
        return HttpUtils.sendPost2(memberUrl + "system/regular/add", JSONObject.toJSONString(map));
    }


    @PostMapping(value = "/regular/delete", produces="application/json;charset=UTF-8")
    public String regularDel(@RequestBody HashMap map,HttpServletRequest request) throws  IOException {
        return HttpUtils.sendPost2(memberUrl + "system/regular/delete", JSONObject.toJSONString(map));
    }

    @GetMapping(value = "/regular/{memberId}", produces="application/json;charset=UTF-8")
    public String regularList(@PathVariable(value = "memberId")int memberId,HttpServletRequest request) {
        return HttpUtils.sendGet(memberUrl + "system/regular/" + memberId);
    }

    @PostMapping(value = "/subMember/register", produces="application/json;charset=UTF-8")
    public String subRegister(@RequestBody HashMap map,HttpServletRequest request) throws IOException {
        if(map.get("hotelId") == null) {
            return JSONObject.toJSONString(AjaxResult.error("集团ID不得为空"));
        }
        Long hotelId;
        try {
            hotelId = (Long) map.get("hotelId");
        }catch (Exception e) {
            return JSONObject.toJSONString(AjaxResult.error("集团Id格式错误"));
        }

        final BaseChainHotel baseChainHotel = baseChainHotelService.selectBaseChainHotelByChotelId(hotelId);
        if(baseChainHotel == null) {
            return JSONObject.toJSONString(AjaxResult.error("该集团不存在"));
        }
        map.put("hotelName",baseChainHotel.getChotelName());
        return HttpUtils.sendPost2(memberUrl + "system/subMember/register", JSONObject.toJSONString(map));
    }



    /* 根据身份证号码查询会员信息，返回会员ID，注册的子账号ID，子账号关联的酒店，手机号码 */
    @GetMapping(value = "/memberInfo", produces="application/json;charset=UTF-8")
    public Object memberList(@RequestParam(value = "memberIdnumber") String idNumber,HttpServletResponse response,HttpServletRequest request) {
        // TODO: 2023/4/3 还要返回该会员常住的酒店
        final String res = HttpUtils.sendGet(memberUrl + "system/member/info?memberIdnumber=" + idNumber);
        final JSONObject jsonObject = JSONObject.parseObject(res);
        if(StringUtils.isNull(jsonObject)) {
            final JSONObject error = new JSONObject();
            error.put("msg","会员模块异常");
            error.put("code","500");
            response.setStatus(HttpStatus.ERROR);
            return error;
        }
        if(!jsonObject.get("code").equals("200")) {
            response.setStatus(HttpStatus.ERROR);
            return jsonObject;
        }
        final JSONObject data = (JSONObject) jsonObject.get("data");
        final JSONArray records = (JSONArray) data.get("records");

        /* 对每一条集团记录增加对应的酒店列表 */
        for (Object record : records) {
            final int hotelId = (Integer) ((JSONObject)record).get("hotelId");
            final BaseChainHotel baseChainHotel = new BaseChainHotel();
            baseChainHotel.setChotelParent((long) hotelId);
            final List<BaseChainHotel> baseChainHotels = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);

            final ArrayList<BaseHotel> baseHotels = new ArrayList<>();

            /* 依次增加酒店 */
            for (BaseChainHotel chainHotel : baseChainHotels) {
                final BaseHotel hotel = baseHotelService.selectBaseHotelByChotelId(chainHotel.getChotelId());
                baseHotels.add(hotel);
            }
            ((JSONObject) record).put("total",baseHotels.size());
            ((JSONObject)record).put("list",baseHotels);
        }
        return jsonObject;
    }
}
