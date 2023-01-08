package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.business.domain.DepartmentFunctionRelationships;
import com.ruoyi.business.service.IDepartmentFunctionRelationshipsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 部门职能连接信息Controller
 *
 * @author ruoyi
 * @date 2022-11-18
 */
@RestController
@RequestMapping("/business/relationships")
public class DepartmentFunctionRelationshipsController extends BaseController
{
    @Autowired
    private IDepartmentFunctionRelationshipsService departmentFunctionRelationshipsService;

    /**
     * 查询部门职能连接信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:relationships:list')")
    @GetMapping("/list")
    public TableDataInfo list(DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        startPage();
        List<DepartmentFunctionRelationships> list = departmentFunctionRelationshipsService.selectDepartmentFunctionRelationshipsList(departmentFunctionRelationships);
        return getDataTable(list);
    }

    /**
     * 导出部门职能连接信息列表
     */
/*    @PreAuthorize("@ss.hasPermi('business:relationships:export')")
    @Log(title = "部门职能连接信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        List<DepartmentFunctionRelationships> list = departmentFunctionRelationshipsService.selectDepartmentFunctionRelationshipsList(departmentFunctionRelationships);
        ExcelUtil<DepartmentFunctionRelationships> util = new ExcelUtil<DepartmentFunctionRelationships>(DepartmentFunctionRelationships.class);
        util.exportExcel(response, list, "部门职能连接信息数据");
    }*/

    /**
     * 获取部门职能连接信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(departmentFunctionRelationshipsService.selectDepartmentFunctionRelationshipsById(id));
    }

    /**
     * 新增部门职能连接信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships:add')")
    @Log(title = "部门职能连接信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        return toAjax(departmentFunctionRelationshipsService.insertDepartmentFunctionRelationships(departmentFunctionRelationships));
    }

    /**
     * 修改部门职能连接信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships:edit')")
    @Log(title = "部门职能连接信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DepartmentFunctionRelationships departmentFunctionRelationships)
    {
        return toAjax(departmentFunctionRelationshipsService.updateDepartmentFunctionRelationships(departmentFunctionRelationships));
    }

    /**
     * 删除部门职能连接信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships:remove')")
    @Log(title = "部门职能连接信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(departmentFunctionRelationshipsService.deleteDepartmentFunctionRelationshipsByIds(ids));
    }
}
