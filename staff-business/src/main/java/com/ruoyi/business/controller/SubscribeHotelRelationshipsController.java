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
import com.ruoyi.business.domain.SubscribeHotelRelationships;
import com.ruoyi.business.service.ISubscribeHotelRelationshipsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 鉴权信息Controller
 *
 * @author ruoyi
 * @date 2023-01-30
 */
@RestController
@RequestMapping("/business/relationships2")
public class SubscribeHotelRelationshipsController extends BaseController
{
    @Autowired
    private ISubscribeHotelRelationshipsService subscribeHotelRelationshipsService;

    /**
     * 查询鉴权信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubscribeHotelRelationships subscribeHotelRelationships)
    {
        startPage();
        List<SubscribeHotelRelationships> list = subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsList(subscribeHotelRelationships);
        return getDataTable(list);
    }

    /**
     * 导出鉴权信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:export')")
    @Log(title = "鉴权信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SubscribeHotelRelationships subscribeHotelRelationships)
    {
        List<SubscribeHotelRelationships> list = subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsList(subscribeHotelRelationships);
        ExcelUtil<SubscribeHotelRelationships> util = new ExcelUtil<SubscribeHotelRelationships>(SubscribeHotelRelationships.class);
        util.exportExcel(response, list, "鉴权信息数据");
    }

    /**
     * 获取鉴权信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(subscribeHotelRelationshipsService.selectSubscribeHotelRelationshipsById(id));
    }

    /**
     * 新增鉴权信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:add')")
    @Log(title = "鉴权信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SubscribeHotelRelationships subscribeHotelRelationships)
    {
        return toAjax(subscribeHotelRelationshipsService.insertSubscribeHotelRelationships(subscribeHotelRelationships));
    }

    /**
     * 修改鉴权信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:edit')")
    @Log(title = "鉴权信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SubscribeHotelRelationships subscribeHotelRelationships)
    {
        return toAjax(subscribeHotelRelationshipsService.updateSubscribeHotelRelationships(subscribeHotelRelationships));
    }

    /**
     * 删除鉴权信息
     */
    @PreAuthorize("@ss.hasPermi('business:relationships2:remove')")
    @Log(title = "鉴权信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(subscribeHotelRelationshipsService.deleteSubscribeHotelRelationshipsByIds(ids));
    }
}
