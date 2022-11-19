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
import com.ruoyi.business.domain.BaseFunction;
import com.ruoyi.business.service.IBaseFunctionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 职能信息Controller
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
@RestController
@RequestMapping("/business/function")
public class BaseFunctionController extends BaseController
{
    @Autowired
    private IBaseFunctionService baseFunctionService;

    /**
     * 查询职能信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:function:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseFunction baseFunction)
    {
        startPage();
        List<BaseFunction> list = baseFunctionService.selectBaseFunctionList(baseFunction);
        return getDataTable(list);
    }

    /**
     * 导出职能信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:function:export')")
    @Log(title = "职能信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseFunction baseFunction)
    {
        List<BaseFunction> list = baseFunctionService.selectBaseFunctionList(baseFunction);
        ExcelUtil<BaseFunction> util = new ExcelUtil<BaseFunction>(BaseFunction.class);
        util.exportExcel(response, list, "职能信息数据");
    }

    /**
     * 获取职能信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:function:query')")
    @GetMapping(value = "/{functionId}")
    public AjaxResult getInfo(@PathVariable("functionId") Long functionId)
    {
        return AjaxResult.success(baseFunctionService.selectBaseFunctionByFunctionId(functionId));
    }

    /**
     * 新增职能信息
     */
    @PreAuthorize("@ss.hasPermi('business:function:add')")
    @Log(title = "职能信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseFunction baseFunction)
    {
        return toAjax(baseFunctionService.insertBaseFunction(baseFunction));
    }

    /**
     * 修改职能信息
     */
    @PreAuthorize("@ss.hasPermi('business:function:edit')")
    @Log(title = "职能信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseFunction baseFunction)
    {
        return toAjax(baseFunctionService.updateBaseFunction(baseFunction));
    }

    /**
     * 删除职能信息
     */
    @PreAuthorize("@ss.hasPermi('business:function:remove')")
    @Log(title = "职能信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{functionIds}")
    public AjaxResult remove(@PathVariable Long[] functionIds)
    {
        return toAjax(baseFunctionService.deleteBaseFunctionByFunctionIds(functionIds));
    }
}
