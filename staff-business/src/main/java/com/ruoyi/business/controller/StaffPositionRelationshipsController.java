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
import com.ruoyi.business.domain.StaffPositionRelationships;
import com.ruoyi.business.service.IStaffPositionRelationshipsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 员工职位关联信息Controller
 * 
 * @author ruoyi
 * @date 2022-11-19
 */
@RestController
@RequestMapping("/business/staffPositionRelationships")
public class StaffPositionRelationshipsController extends BaseController
{
    @Autowired
    private IStaffPositionRelationshipsService staffPositionRelationshipsService;

    /**
     * 查询员工职位关联信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:list')")
    @GetMapping("/list")
    public TableDataInfo list(StaffPositionRelationships staffPositionRelationships)
    {
        startPage();
        List<StaffPositionRelationships> list = staffPositionRelationshipsService.selectStaffPositionRelationshipsList(staffPositionRelationships);
        return getDataTable(list);
    }

    /**
     * 导出员工职位关联信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:export')")
    @Log(title = "员工职位关联信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StaffPositionRelationships staffPositionRelationships)
    {
        List<StaffPositionRelationships> list = staffPositionRelationshipsService.selectStaffPositionRelationshipsList(staffPositionRelationships);
        ExcelUtil<StaffPositionRelationships> util = new ExcelUtil<StaffPositionRelationships>(StaffPositionRelationships.class);
        util.exportExcel(response, list, "员工职位关联信息数据");
    }

    /**
     * 获取员工职位关联信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(staffPositionRelationshipsService.selectStaffPositionRelationshipsById(id));
    }

    /**
     * 新增员工职位关联信息
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:add')")
    @Log(title = "员工职位关联信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StaffPositionRelationships staffPositionRelationships)
    {
        return toAjax(staffPositionRelationshipsService.insertStaffPositionRelationships(staffPositionRelationships));
    }

    /**
     * 修改员工职位关联信息
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:edit')")
    @Log(title = "员工职位关联信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StaffPositionRelationships staffPositionRelationships)
    {
        return toAjax(staffPositionRelationshipsService.updateStaffPositionRelationships(staffPositionRelationships));
    }

    /**
     * 删除员工职位关联信息
     */
    @PreAuthorize("@ss.hasPermi('business:staffPositionRelationships:remove')")
    @Log(title = "员工职位关联信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(staffPositionRelationshipsService.deleteStaffPositionRelationshipsByIds(ids));
    }
}
