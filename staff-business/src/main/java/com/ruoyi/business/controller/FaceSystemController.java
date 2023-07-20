package com.ruoyi.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.annotations.StaffTokenCheck;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.sign.Base64;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/face",produces = "application/json;charset=UTF-8")
@StaffTokenCheck
public class FaceSystemController {

    @Value("${hotel.face.url}")
    private String faceUrl;

    @Autowired
    private RestTemplate restTemplate;

    /* 人脸验证 */
    @PostMapping(value = "/compare")
    public String compareFaces(@RequestParam("image1") MultipartFile image1, @RequestParam("image2") MultipartFile image2) throws IOException {
        final String image11 = java.util.Base64.getEncoder().encodeToString(image1.getBytes());
        final String image22 = java.util.Base64.getEncoder().encodeToString(image2.getBytes());
        final String encode1 = URLEncoder.encode(image11, "UTF-8");
        final String encode2 = URLEncoder.encode(image22, "UTF-8");
        return HttpUtils.sendPost(faceUrl + "face/compare","image1=" + encode1 + "&image2=" + encode2);
    }

    /* 人脸比对并上报入住信息到PSB */
    @PostMapping("/reportStayInToPsb/{hotelNumber}")
    public String reportStayInToPsb(@RequestParam("faceImage") MultipartFile faceImage, @RequestParam("idCardImage") MultipartFile idCardImage, @RequestParam("msg") String msg, @PathVariable(value = "hotelNumber") String hotelNumber) throws IOException {

        // 创建HTTP头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 创建HTTP请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("faceImage", new ByteArrayResource(faceImage.getBytes()) {
            @Override
            public String getFilename() {
                return faceImage.getOriginalFilename();
            }
        });
        body.add("idCardImage", new ByteArrayResource(idCardImage.getBytes()) {
            @Override
            public String getFilename() {
                return idCardImage.getOriginalFilename();
            }
        });
        body.add("msg",msg);

        // 创建HTTP请求实体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送HTTP请求
        ResponseEntity<String> response = restTemplate.postForEntity(faceUrl + "face/reportStayInToPsb/" + hotelNumber, requestEntity, String.class);
        return response.getBody();
    }

    /* 退房 */
    @PostMapping("/reportCheckoutToPsb/{hotelNumber}")
    public String reportCheckoutToPsb(@RequestBody HashMap msg, @PathVariable(value = "hotelNumber") String hotelNumber) throws IOException {
        return HttpUtils.sendPost2(faceUrl + "face/reportCheckoutToPsb/" + hotelNumber,JSONObject.toJSONString(msg));
    }

    /* 换房 */
    @PostMapping("/reportChangeRoomsToPsb/{hotelNumber}")
    public String reportChangeRoomsToPsb(@RequestBody HashMap msg, @PathVariable(value = "hotelNumber") String hotelNumber) throws IOException {
        return HttpUtils.sendPost2(faceUrl + "face/reportChangeRoomsToPsb/" + hotelNumber,JSONObject.toJSONString(msg));
    }

    /* 增加酒店对应关系 */
    @PostMapping("/relation/add")
    public String add(@RequestBody HashMap map) throws IOException {
        return HttpUtils.sendPost2(faceUrl + "system/number",JSONObject.toJSONString(map));
    }
}
