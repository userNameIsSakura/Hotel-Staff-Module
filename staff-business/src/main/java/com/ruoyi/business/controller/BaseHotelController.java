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
import com.ruoyi.business.domain.BaseHotel;
import com.ruoyi.business.service.IBaseHotelService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店列表Controller
 * 
 * @author ruoyi
 * @date 2023-01-06
 */
@RestController
@RequestMapping("/business/hotel")
public class BaseHotelController extends BaseController
{
    @Autowired
    private IBaseHotelService baseHotelService;

    /**
     * 查询酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseHotel baseHotel)
    {
        startPage();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return getDataTable(list);
    }

    /**
     * 导出酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:export')")
    @Log(title = "酒店列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseHotel baseHotel)
    {
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        ExcelUtil<BaseHotel> util = new ExcelUtil<BaseHotel>(BaseHotel.class);
        util.exportExcel(response, list, "酒店列表数据");
    }

    /**
     * 获取酒店列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:query')")
    @GetMapping(value = "/{hotelId}")
    public AjaxResult getInfo(@PathVariable("hotelId") Long hotelId)
    {
        return AjaxResult.success(baseHotelService.selectBaseHotelByHotelId(hotelId));
    }

    /**
     * 新增酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:add')")
    @Log(title = "酒店列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseHotel baseHotel)
    {
        return toAjax(baseHotelService.insertBaseHotel(baseHotel));
    }

    /**
     * 修改酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:edit')")
    @Log(title = "酒店列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseHotel baseHotel)
    {
        return toAjax(baseHotelService.updateBaseHotel(baseHotel));
    }

    /**
     * 删除酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:hotel:remove')")
    @Log(title = "酒店列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{hotelIds}")
    public AjaxResult remove(@PathVariable Long[] hotelIds)
    {
        return toAjax(baseHotelService.deleteBaseHotelByHotelIds(hotelIds));
    }
}
