package com.ruoyi.business.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InterfaceController {

    @Autowired
    private ISysUserService sysUserService;


    @PostMapping("/getHotelId")
    private String getHotelIdByAdministratorId(@RequestBody String administratorId) {
        SysUser sysUser = sysUserService.selectUserById(Long.valueOf(administratorId.split("=")[1]));
        if(sysUser == null || sysUser.getSuperAdministrator() == 1)
            return "0";
        Long hotelId = sysUser.getHotelId();

        return String.valueOf(hotelId);
    }
}
