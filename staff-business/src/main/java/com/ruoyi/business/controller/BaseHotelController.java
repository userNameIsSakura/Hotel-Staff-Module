package com.ruoyi.business.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private IBaseChainHotelService baseChainHotelService;
    @Value("${ruoyi.profile}")
    private String uploadPathPrefix;

    /**
     * 查询酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseHotel baseHotel)
    {
        startPage();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return getDataTable(list);
    }


    /**
     * 查询酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/listAll")
    public List<BaseHotel> listAll()
    {
        BaseHotel baseHotel = new BaseHotel();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return list;
    }


    /**
     * 查询集团下酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:user:list')")
    @GetMapping("/listHotels")
    public List<BaseHotel> listHotelAll()
    {
        final BaseChainHotel baseChainHotel = new BaseChainHotel();
        baseChainHotel.setChotelParent(SecurityUtils.getHotelId());
        final List<BaseChainHotel> baseChainHotels = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);
        final ArrayList<BaseHotel> baseHotels = new ArrayList<>();
        for (BaseChainHotel chainHotel : baseChainHotels) {
            final BaseHotel hotel = baseHotelService.selectBaseHotelByChotelId(chainHotel.getChotelId());
            baseHotels.add(hotel);
        }
        return baseHotels;
    }


    public List<BaseHotel> listAllPrivate()
    {
        BaseHotel baseHotel = new BaseHotel();
        List<BaseHotel> list = baseHotelService.selectBaseHotelList(baseHotel);
        return list;
    }

    /**
     * 导出酒店列表列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:export')")
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
    @PreAuthorize("@ss.hasPermi('business:chainHotel:query')")
    @GetMapping(value = "/{hotelId}")
    public AjaxResult getInfo(@PathVariable("hotelId") Long hotelId)
    {
        return AjaxResult.success(baseHotelService.selectBaseHotelByHotelId(hotelId));
    }

    /**
     * 获取酒店列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:query')")
    @GetMapping(value = "/chotelId/{chotelId}")
    public AjaxResult getInfoByChotelId(@PathVariable("chotelId") Long chotelId)
    {
        return AjaxResult.success(baseHotelService.selectBaseHotelByChotelId(chotelId));
    }

    /**
     * 新增酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:add')")
    @Log(title = "酒店列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseHotel baseHotel)
    {
        final BaseHotel baseHotel1 = new BaseHotel();
        baseHotel1.setHotelName(baseHotel.getHotelName());
        if ((baseHotelService.selectBaseHotelList(baseHotel1).size() > 0)) {
            return AjaxResult.error("酒店名已存在");
        }
        return toAjax(baseHotelService.insertBaseHotel(baseHotel));
    }

    /**
     * 修改酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:edit')")
    @Log(title = "酒店列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseHotel baseHotel)
    {
        return toAjax(baseHotelService.updateBaseHotel(baseHotel));
    }

    /**
     * 删除酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:remove')")
    @Log(title = "酒店列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{hotelIds}")
    public AjaxResult remove(@PathVariable Long[] hotelIds)
    {
        return toAjax(baseHotelService.deleteBaseHotelByHotelIds(hotelIds));
    }
}
