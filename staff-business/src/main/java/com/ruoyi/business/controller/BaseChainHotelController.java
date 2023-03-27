package com.ruoyi.business.controller;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
import com.ruoyi.business.domain.BaseChainHotel;
import com.ruoyi.business.service.IBaseChainHotelService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 连锁酒店Controller
 *
 * @author ruoyi
 * @date 2023-03-13
 */
@RestController
@RequestMapping("/business/chainHotel")
public class BaseChainHotelController extends BaseController
{
    @Autowired
    private IBaseChainHotelService baseChainHotelService;
    @Autowired
    private IBaseHotelService baseHotelService;



    /**
     * 查询连锁酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:list')")
    @GetMapping("/list")
    public AjaxResult list(BaseChainHotel baseChainHotel)
    {
        List<BaseChainHotel> list = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);
        return AjaxResult.success(list);
    }

    /**
     * 获得集团ID
     *
     * @return int
     */
    @GetMapping
    public Long getChotelId() {
        if(SecurityUtils.getHotelId() != null)
            return baseChainHotelService.selectBaseChainHotelByHotelId(SecurityUtils.getHotelId());
        else
            return 0L;
    }


    /**
     * 导出连锁酒店列表
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:export')")
    @Log(title = "连锁酒店", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseChainHotel baseChainHotel)
    {
        List<BaseChainHotel> list = baseChainHotelService.selectBaseChainHotelList(baseChainHotel);
        ExcelUtil<BaseChainHotel> util = new ExcelUtil<BaseChainHotel>(BaseChainHotel.class);
        util.exportExcel(response, list, "连锁酒店数据");
    }

    /**
     * 获取连锁酒店详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:query')")
    @GetMapping(value = "/{chotelId}")
    public AjaxResult getInfo(@PathVariable("chotelId") Long chotelId)
    {
        return AjaxResult.success(baseChainHotelService.selectBaseChainHotelByChotelId(chotelId));
    }

    /**
     * 新增连锁酒店
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:add')")
    @Log(title = "连锁酒店", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseChainHotel baseChainHotel)
    {
        return toAjax(baseChainHotelService.insertBaseChainHotel(baseChainHotel));
    }

    /**
     * 修改连锁酒店
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:edit')")
    @Log(title = "连锁酒店", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseChainHotel baseChainHotel)
    {
        return toAjax(baseChainHotelService.updateBaseChainHotel(baseChainHotel));
    }

    /**
     * 删除连锁酒店
     */
    @PreAuthorize("@ss.hasPermi('business:chainHotel:remove')")
    @Log(title = "连锁酒店", businessType = BusinessType.DELETE)
	@DeleteMapping("/{chotelIds}")
    public AjaxResult remove(@PathVariable Long[] chotelIds)
    {
        return toAjax(baseChainHotelService.deleteBaseChainHotelByChotelIds(chotelIds));
    }
}
