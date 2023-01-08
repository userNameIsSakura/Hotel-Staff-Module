package com.ruoyi.business.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.domain.BaseAuth;
import com.ruoyi.business.service.IBaseAuthService;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BaseRole;
import com.ruoyi.business.service.IBaseRoleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 角色信息Controller
 *
 * @author ruoyi
 * @date 2023-01-07
 */
@RestController
@RequestMapping("/business/role")
public class BaseRoleController extends BaseController
{
    @Autowired
    private IBaseRoleService baseRoleService;
    @Autowired
    private IBaseAuthService baseAuthService;

    /**
     * 查询角色信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseRole baseRole)
    {
        startPage();
        baseRole.setHotelId(SecurityUtils.getHotelId());
        List<BaseRole> list = baseRoleService.selectBaseRoleList(baseRole);
        return getDataTable(list);
    }

    /**
     * 导出角色信息列表
     */
/*    @PreAuthorize("@ss.hasPermi('business:role:export')")
    @Log(title = "角色信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseRole baseRole)
    {
        List<BaseRole> list = baseRoleService.selectBaseRoleList(baseRole);
        ExcelUtil<BaseRole> util = new ExcelUtil<BaseRole>(BaseRole.class);
        util.exportExcel(response, list, "角色信息数据");
    }*/

    /**
     * 获取角色信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:role:query')")
    @GetMapping(value = {"/","/{roleId}"})
    public AjaxResult getInfo(@PathVariable(value = "roleId", required = false) Long roleId)
    {
        AjaxResult success = AjaxResult.success(baseRoleService.selectBaseRoleByRoleId(roleId));
        BaseAuth baseAuth = new BaseAuth();
        baseAuth.setHotelId(SecurityUtils.getHotelId());
        List<BaseAuth> baseAuths = baseAuthService.selectBaseAuthList(baseAuth);
        success.put("authList",baseAuths);

        List<Long> auths = baseAuthService.selectBaseAuthByRoleId(roleId).stream().map(BaseAuth::getAuthId).collect(Collectors.toList());
        success.put("auths",auths);

        return success;
    }

    /**
     * 新增角色信息
     */
    @PreAuthorize("@ss.hasPermi('business:role:add')")
    @Log(title = "角色信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseRole baseRole)
    {
        baseRole.setHotelId(SecurityUtils.getHotelId());
        return toAjax(baseRoleService.insertBaseRole(baseRole));
    }

    /**
     * 修改角色信息
     */
    @PreAuthorize("@ss.hasPermi('business:role:edit')")
    @Log(title = "角色信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseRole baseRole)
    {
        return toAjax(baseRoleService.updateBaseRole(baseRole));
    }

    /**
     * 删除角色信息
     */
    @PreAuthorize("@ss.hasPermi('business:role:remove')")
    @Log(title = "角色信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(baseRoleService.deleteBaseRoleByRoleIds(roleIds));
    }
}
