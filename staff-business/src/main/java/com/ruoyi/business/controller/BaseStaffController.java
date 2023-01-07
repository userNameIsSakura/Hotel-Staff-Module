package com.ruoyi.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.domain.BaseRole;
import com.ruoyi.business.domain.StaffRoleRelationships;
import com.ruoyi.business.service.IBaseRoleService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BaseStaff;
import com.ruoyi.business.service.IBaseStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工信息Controller
 *
 * @author ruoyi
 * @date 2022-11-10
 */
@RestController
@RequestMapping("/business/staff")
public class BaseStaffController extends BaseController
{
    @Autowired
    private IBaseStaffService baseStaffService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IBaseRoleService baseRoleService;

    /**
     * 查询员工信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseStaff baseStaff)
    {
        startPage();

        baseStaff.setHotelId(SecurityUtils.getHotelId());

        List<BaseStaff> list = baseStaffService.selectBaseStaffList(baseStaff);
        return getDataTable(list);
    }

    @PostMapping("/login")
    public String login(@RequestBody HashMap map)
    {
        String phone = (String) map.get("phone");
        String password = (String) map.get("password");

        /* 账号密码验证 */
        BaseStaff baseStaff = new BaseStaff();
        baseStaff.setStaffPhone(phone);
        baseStaff.setStaffPassword(password);
        List<BaseStaff> baseStaffs = baseStaffService.selectBaseStaffList(baseStaff);
        if(baseStaffs.size() == 0)
            return null;
        else
            return tokenService.createStaffToken(baseStaffs.get(0).getStaffId());
    }


    @PostMapping("/auth")
    public boolean auth(@RequestBody String url,HttpServletRequest request) {
        Long staffId = tokenService.getStaffUser(request).getStaffId();
        int i = baseStaffService.checkUrl(staffId, url);
        if(i > 0)
            return true;
        return false;
    }

    /**
     * 导出员工信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:staff:export')")
    @Log(title = "员工信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseStaff baseStaff)
    {
        List<BaseStaff> list = baseStaffService.selectBaseStaffList(baseStaff);
        ExcelUtil<BaseStaff> util = new ExcelUtil<BaseStaff>(BaseStaff.class);
        util.exportExcel(response, list, "员工信息数据");
    }



    /**
     * 获取员工信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:query')")
    @GetMapping(value = {"/","/{staffId}"})
    public AjaxResult getInfo(@PathVariable(value = "staffId",required = false) Long staffId)
    {
        AjaxResult success = AjaxResult.success(baseStaffService.selectBaseStaffByStaffId(staffId));

        BaseRole baseRole = new BaseRole();
        baseRole.setHotelId(SecurityUtils.getHotelId());
        List<BaseRole> baseRoles = baseRoleService.selectBaseRoleList(baseRole);
        success.put("roleList",baseRoles);

        List<Long> roles = baseRoleService.selectBaseRoleByStaffId(staffId).stream().map(BaseRole::getRoleId).collect(Collectors.toList());
        success.put("roles",roles);

        return success;
    }

    /**
     * 新增员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:add')")
    @Log(title = "员工信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseStaff baseStaff)
    {
        baseStaff.setHotelId(SecurityUtils.getHotelId());

        return toAjax(baseStaffService.insertBaseStaff(baseStaff));
    }

    /**
     * 修改员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:edit')")
    @Log(title = "员工信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseStaff baseStaff)
    {
        return toAjax(baseStaffService.updateBaseStaff(baseStaff));
    }

    /**
     * 删除员工信息
     */
    @PreAuthorize("@ss.hasPermi('business:staff:remove')")
    @Log(title = "员工信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(baseStaffService.deleteBaseStaffByStaffIds(staffIds));
    }
}
