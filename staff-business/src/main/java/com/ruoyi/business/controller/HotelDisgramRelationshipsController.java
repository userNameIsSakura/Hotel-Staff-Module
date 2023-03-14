package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.business.domain.HotelDisgramRelationships;
import com.ruoyi.business.service.IHotelDisgramRelationshipsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店介绍图Controller
 *
 * @author ruoyi
 * @date 2023-03-14
 */
@RestController
@RequestMapping("/business/diagram")
public class HotelDisgramRelationshipsController extends BaseController
{
    @Autowired
    private IHotelDisgramRelationshipsService hotelDisgramRelationshipsService;

    /**
     * 查询酒店介绍图列表
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:list')")
    @GetMapping("/list")
    public TableDataInfo list(HotelDisgramRelationships hotelDisgramRelationships)
    {
        startPage();
        List<HotelDisgramRelationships> list = hotelDisgramRelationshipsService.selectHotelDisgramRelationshipsList(hotelDisgramRelationships);
        return getDataTable(list);
    }

    /**
     * 导出酒店介绍图列表
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:export')")
    @Log(title = "酒店介绍图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HotelDisgramRelationships hotelDisgramRelationships)
    {
        List<HotelDisgramRelationships> list = hotelDisgramRelationshipsService.selectHotelDisgramRelationshipsList(hotelDisgramRelationships);
        ExcelUtil<HotelDisgramRelationships> util = new ExcelUtil<HotelDisgramRelationships>(HotelDisgramRelationships.class);
        util.exportExcel(response, list, "酒店介绍图数据");
    }

    /**
     * 获取酒店介绍图详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hotelDisgramRelationshipsService.selectHotelDisgramRelationshipsById(id));
    }

    /**
     * 新增酒店介绍图
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:add')")
    @Log(title = "酒店介绍图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HotelDisgramRelationships hotelDisgramRelationships)
    {
        hotelDisgramRelationships.setHotelId(SecurityUtils.getHotelId());
        return toAjax(hotelDisgramRelationshipsService.insertHotelDisgramRelationships(hotelDisgramRelationships));
    }

    /**
     * 修改酒店介绍图
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:edit')")
    @Log(title = "酒店介绍图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HotelDisgramRelationships hotelDisgramRelationships)
    {
        return toAjax(hotelDisgramRelationshipsService.updateHotelDisgramRelationships(hotelDisgramRelationships));
    }

    /**
     * 删除酒店介绍图
     */
    @PreAuthorize("@ss.hasPermi('business:diagram:remove')")
    @Log(title = "酒店介绍图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hotelDisgramRelationshipsService.deleteHotelDisgramRelationshipsByIds(ids));
    }
}
