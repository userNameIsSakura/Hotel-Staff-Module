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

    /* 会员主账号 */
    @PostMapping(value = "/member/register", produces="application/json;charset=UTF-8")
    public String register(@RequestBody HashMap map, HttpServletRequest request) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/member/register", JSONObject.toJSONString(map));
    }

    /* 会员子账号 */
    @PostMapping(value = "/subMember/register", produces="application/json;charset=UTF-8")
    public String subRegister(@RequestBody HashMap map) throws IOException {
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

    /* 常住酒店相关 */
    @PostMapping(value = "/regular/add", produces="application/json;charset=UTF-8")
    public String regularAdd(@RequestBody HashMap map) throws  IOException {
        return HttpUtils.sendPost2(memberUrl + "system/regular/add", JSONObject.toJSONString(map));
    }


    @PostMapping(value = "/regular/delete", produces="application/json;charset=UTF-8")
    public String regularDel(@RequestBody HashMap map) throws  IOException {
        return HttpUtils.sendPost2(memberUrl + "system/regular/delete", JSONObject.toJSONString(map));
    }

    @GetMapping(value = "/regular/{memberId}", produces="application/json;charset=UTF-8")
    public String regularList(@PathVariable(value = "memberId")int memberId) {
        final String json = HttpUtils.sendGet(memberUrl + "system/regular/" + memberId);

        final JSONObject jsonObject = JSONObject.parseObject(json);

        if(Integer.parseInt(jsonObject.get("code").toString()) != 200) {
            return json;
        }

        try {
            final ArrayList<Integer> data = (ArrayList<Integer>) jsonObject.get("data");

            final List<BaseHotel> baseHotels = baseHotelService.selectBaseHotelByHotelIdArray(data);

            return JSONObject.toJSONString(AjaxResult.success(baseHotels));
        }catch (Exception e) {
            return json;
        }
    }

    /* 根据身份证号码查询会员信息，返回会员ID，注册的子账号ID，子账号关联的酒店，手机号码 */
    @GetMapping(value = "/memberInfo", produces="application/json;charset=UTF-8")
    public Object memberList(@RequestParam(value = "memberIdnumber",required = false) String idNumber,@RequestParam(value = "memberPhone",required = false) String memberPhone, HttpServletResponse response) {
        // TODO: 2023/4/3 还要返回该会员常住的酒店
        String param = "";
        if(idNumber != null) {
            param = "memberIdnumber=" + idNumber + "&";
        }
        if(memberPhone != null) {
            param = "memberPhone=" + memberPhone;
        }
        if(param.endsWith("&")) {
            param = param.substring(0,param.length() - 1);
        }

        final String res = HttpUtils.sendGet(memberUrl + "system/member/info",param);
        final JSONObject jsonObject = JSONObject.parseObject(res);
        if(StringUtils.isNull(jsonObject)) {
            final JSONObject error = new JSONObject();
            error.put("msg","会员模块异常");
            error.put("code","500");
            response.setStatus(HttpStatus.ERROR);
            return error;
        }



        if(!jsonObject.get("code").equals("200") && Integer.parseInt(jsonObject.get("code").toString()) != 200) {
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

    /* 资金池相关接口 */
    @PostMapping(value = "/pool/recharge",produces="application/json;charset=UTF-8")
    public String poolRecharge(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/pool/recharge",JSONObject.toJSONString(map));
    }

    @PostMapping(value = "/pool/consumption",produces="application/json;charset=UTF-8")
    public String poolConsumption(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/pool/consumption",JSONObject.toJSONString(map));
    }

    @GetMapping(value = "/pool/balance",produces="application/json;charset=UTF-8")
    public String poolBalance(@RequestParam(value = "memberPhone",required = false) String memberPhone,@RequestParam(value = "memberIdnumber",required = false) String memberIdnumber,@RequestParam(value = "hotelId") Long hotelId) throws IOException {
        String url = memberUrl + "system/pool/balance";
        String params = "";
        if(memberPhone != null){
            params += "memberPhone=" + memberPhone + "&";
        }
        if(memberIdnumber != null){
            params += "memberIdnumber=" + memberIdnumber + "&";
        }
        params += "hotelId=" + hotelId;
        return HttpUtils.sendGet(url,params);
    }

    @GetMapping(value = "/pool/log",produces="application/json;charset=UTF-8")
    public String poolLog(@RequestParam(value = "memberPhone",required = false) String memberPhone,@RequestParam(value = "memberIdnumber",required = false) String memberIdnumber,@RequestParam(value = "hotelId") Long hotelId) throws IOException {
        String url = memberUrl + "system/pool/log";
        String params = "";
        if(memberPhone != null){
            params += "memberPhone=" + memberPhone + "&";
        }
        if(memberIdnumber != null){
            params += "memberIdnumber=" + memberIdnumber + "&";
        }
        params += "hotelId=" + hotelId;
        return HttpUtils.sendGet(url,params);
    }

    /* 优惠券相关接口 */
    @GetMapping(value = "/coupon/list",produces="application/json;charset=UTF-8")
    public String couponList(@RequestParam(value = "memberPhone",required = false) String memberPhone,@RequestParam(value = "memberIdnumber",required = false) String memberIdnumber,@RequestParam(value = "hotelId",required = false) Long hotelId) throws IOException {
        String url = memberUrl + "system/coupon/list";
        String params = "";
        if(memberPhone != null){
            params += "memberPhone=" + memberPhone + "&";
        }
        if(memberIdnumber != null){
            params += "memberIdnumber=" + memberIdnumber + "&";
        }
        if(hotelId != null){
            params += "hotelId=" + hotelId;
        }
        if(params.endsWith("&")){
            params = params.substring(0,params.length()-1);
        }
        return HttpUtils.sendGet(url,params);
    }

    @PostMapping(value = "/coupon/use",produces="application/json;charset=UTF-8")
    public String couponUse(@RequestBody HashMap map) throws IOException{
        return HttpUtils.sendPost2(memberUrl + "system/coupon/use",JSONObject.toJSONString(map));
    }

    /* 预定等信息 */
    @PostMapping(value = "/reservation/list",produces="application/json;charset=UTF-8")
    public String reservationList(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/reservation/list",JSONObject.toJSONString(map));
    }
    @PostMapping(value = "/reservation/add",produces="application/json;charset=UTF-8")
    public String reservationAdd(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/reservation/add",JSONObject.toJSONString(map));
    }
    @GetMapping("/reservation/{id}")
    public String reservationRemove(@PathVariable(value = "id")String id) {
        return HttpUtils.sendGet(memberUrl + "system/reservation/" + id);
    }

    @PostMapping(value = "/arrangement/list",produces="application/json;charset=UTF-8")
    public String arrangementList(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/arrangement/list",JSONObject.toJSONString(map));
    }
    @PostMapping(value = "/arrangement/add",produces="application/json;charset=UTF-8")
    public String arrangementAdd(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/arrangement/add",JSONObject.toJSONString(map));
    }
    @GetMapping("/arrangement/{id}")
    public String arrangementRemove(@PathVariable(value = "id")String id) {
        return HttpUtils.sendGet(memberUrl + "system/arrangement/" + id);
    }

    @PostMapping(value = "/checkIn/list",produces="application/json;charset=UTF-8")
    public String checkInList(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/checkIn/list",JSONObject.toJSONString(map));
    }
    @PostMapping(value = "/checkIn/add",produces="application/json;charset=UTF-8")
    public String checkInAdd(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/checkIn/add",JSONObject.toJSONString(map));
    }
    @GetMapping(value = "/checkIn/{id}",produces="application/json;charset=UTF-8")
    public String checkInRemove(@PathVariable(value = "id")String id) {
        return HttpUtils.sendGet(memberUrl + "system/checkIn/" + id);
    }
    @PostMapping(value = "/checkIn/latest",produces="application/json;charset=UTF-8")
    public String checkInLatest(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(memberUrl + "system/checkIn/latest",JSONObject.toJSONString(map));
    }
}
