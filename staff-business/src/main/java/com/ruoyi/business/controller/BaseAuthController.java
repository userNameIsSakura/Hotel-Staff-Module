package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.service.IBaseHotelService;
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
import com.ruoyi.business.domain.BaseAuth;
import com.ruoyi.business.service.IBaseAuthService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 权限信息Controller
 *
 * @author ruoyi
 * @date 2023-01-07
 */
@RestController
@RequestMapping("/business/auth")
public class BaseAuthController extends BaseController
{
    @Autowired
    private IBaseAuthService baseAuthService;
    @Autowired
    private IBaseHotelService baseHotelService;

    /**
     * 查询权限信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:auth:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseAuth baseAuth)
    {
        startPage();
        baseAuth.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseAuth.getHotelId()).getHotelId());
        List<BaseAuth> list = baseAuthService.selectBaseAuthList(baseAuth);
        return getDataTable(list);
    }

    /**
     * 导出权限信息列表
     */
/*    @PreAuthorize("@ss.hasPermi('business:auth:export')")
    @Log(title = "权限信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseAuth baseAuth)
    {
        List<BaseAuth> list = baseAuthService.selectBaseAuthList(baseAuth);
        ExcelUtil<BaseAuth> util = new ExcelUtil<BaseAuth>(BaseAuth.class);
        util.exportExcel(response, list, "权限信息数据");
    }*/

    /**
     * 获取权限信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:auth:query')")
    @GetMapping(value = "/{authId}")
    public AjaxResult getInfo(@PathVariable("authId") Long authId)
    {
        return AjaxResult.success(baseAuthService.selectBaseAuthByAuthId(authId));
    }

    /**
     * 新增权限信息
     */
    @PreAuthorize("@ss.hasPermi('business:auth:add')")
    @Log(title = "权限信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseAuth baseAuth)
    {
        baseAuth.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseAuth.getHotelId()).getHotelId());
        int rows = baseAuthService.insertBaseAuth(baseAuth);
        return toAjax(rows);
    }

    /**
     * 修改权限信息
     */
    @PreAuthorize("@ss.hasPermi('business:auth:edit')")
    @Log(title = "权限信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseAuth baseAuth)
    {
        baseAuth.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseAuth.getHotelId()).getHotelId());
        return toAjax(baseAuthService.updateBaseAuth(baseAuth));
    }

    /**
     * 删除权限信息
     */
    @PreAuthorize("@ss.hasPermi('business:auth:remove')")
    @Log(title = "权限信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{authIds}")
    public AjaxResult remove(@PathVariable Long[] authIds)
    {
        return toAjax(baseAuthService.deleteBaseAuthByAuthIds(authIds));
    }
}
