package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.business.domain.BaseDepartment;
import com.ruoyi.business.service.IBaseDepartmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 部门信息Controller
 *
 * @author ruoyi
 * @date 2022-11-12
 */
@RestController
@RequestMapping("/business/department")
public class BaseDepartmentController extends BaseController
{
    @Autowired
    private IBaseDepartmentService baseDepartmentService;

    @Autowired
    private IBaseHotelService baseHotelService;

    /**
     * 查询部门信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:department:list')")
    @GetMapping("/list")
    public AjaxResult list(BaseDepartment baseDepartment)
    {

        baseDepartment.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseDepartment.getHotelId()).getHotelId());

        List<BaseDepartment> list = baseDepartmentService.selectBaseDepartmentList(baseDepartment);
        return AjaxResult.success(list);
    }

    /**
     * 导出部门信息列表
     */
/*    @PreAuthorize("@ss.hasPermi('business:department:export')")
    @Log(title = "部门信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseDepartment baseDepartment)
    {
        List<BaseDepartment> list = baseDepartmentService.selectBaseDepartmentList(baseDepartment);
        ExcelUtil<BaseDepartment> util = new ExcelUtil<BaseDepartment>(BaseDepartment.class);
        util.exportExcel(response, list, "部门信息数据");
    }*/

    /**
     * 获取部门信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:department:query')")
    @GetMapping(value = "/{departmentId}")
    public AjaxResult getInfo(@PathVariable("departmentId") Long departmentId)
    {
        return AjaxResult.success(baseDepartmentService.selectBaseDepartmentByDepartmentId(departmentId));
    }

    /**
     * 新增部门信息
     */
    @PreAuthorize("@ss.hasPermi('business:department:add')")
    @Log(title = "部门信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseDepartment baseDepartment)
    {
        if(baseDepartment.getSuperiorId() == null)
            baseDepartment.setSuperiorId(-1l);

        baseDepartment.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseDepartment.getHotelId()).getHotelId());

        return toAjax(baseDepartmentService.insertBaseDepartment(baseDepartment));
    }

    /**
     * 修改部门信息
     */
    @PreAuthorize("@ss.hasPermi('business:department:edit')")
    @Log(title = "部门信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseDepartment baseDepartment)
    {
        baseDepartment.setHotelId(baseHotelService.selectBaseHotelByChotelId(baseDepartment.getHotelId()).getHotelId());

        return toAjax(baseDepartmentService.updateBaseDepartment(baseDepartment));
    }

    /**
     * 删除部门信息
     */
    @PreAuthorize("@ss.hasPermi('business:department:remove')")
    @Log(title = "部门信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{departmentIds}")
    public AjaxResult remove(@PathVariable Long[] departmentIds)
    {
        return toAjax(baseDepartmentService.deleteBaseDepartmentByDepartmentIds(departmentIds));
    }
}
