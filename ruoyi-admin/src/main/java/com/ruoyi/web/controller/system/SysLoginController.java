package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.core.domain.entity.SysAdministrator;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysMenuService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

/*

    public AjaxResult login(@RequestBody LoginBody loginBody, HttpServletResponse response,HttpServletRequest request)
    {
        System.out.println();
        SysAdministrator login = loginService.login(loginBody.getUsername(), loginBody.getPassword());
        if(login == null)
        {
            loginBody.setUsername("admin");
            loginBody.setPassword("aaa");
        }
        else
        {
            loginBody.setUsername("admin");
            loginBody.setPassword("admin123");
            HttpSession session = request.getSession();
            session.setAttribute("hotel",login.getHotelId());
            Cookie admin = new Cookie("admin", login.getSuperAdministrator().toString());
            admin.setPath("/");
            response.addCookie(admin);
        }
        return login2(loginBody,login);
    }
*/

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody,HttpServletResponse response) throws InterruptedException {

        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(),true);

        ajax.put(Constants.TOKEN, token);

        return ajax;
    }

    @PostMapping("/loginInterface")
    public AjaxResult loginInterface(@RequestBody LoginBody loginBody,HttpServletResponse response) throws InterruptedException {

        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid(),false);

        ajax.put(Constants.TOKEN, token);

        return ajax;
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
