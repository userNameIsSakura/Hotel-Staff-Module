package com.ruoyi.business.aop;

import com.ruoyi.common.core.domain.model.StaffUser;
import com.ruoyi.framework.web.service.TokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class StaffTokenCheckAspect {

    @Autowired
    private TokenService tokenService;


    @Pointcut("@within(com.ruoyi.business.annotations.StaffTokenCheck) || @annotation(com.ruoyi.business.annotations.StaffTokenCheck)")
    public void tokenCheckPointCut() {}

    @Before("tokenCheckPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StaffUser staffUser = tokenService.getStaffUser(request);
        if(staffUser == null) {
            throw new RuntimeException("权限验证失败");
        }
    }


}
