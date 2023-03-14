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
import com.ruoyi.business.domain.BaseDiagram;
import com.ruoyi.business.service.IBaseDiagramService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 介绍图类别Controller
 *
 * @author ruoyi
 * @date 2023-03-14
 */
@RestController
@RequestMapping("/business/diagramType")
public class BaseDiagramController extends BaseController
{
    @Autowired
    private IBaseDiagramService baseDiagramService;

    /**
     * 查询介绍图类别列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BaseDiagram baseDiagram)
    {
        startPage();
        List<BaseDiagram> list = baseDiagramService.selectBaseDiagramList(baseDiagram);
        return getDataTable(list);
    }

    /**
     * 导出介绍图类别列表
     */
    @PreAuthorize("@ss.hasPermi('business:diagramType:export')")
    @Log(title = "介绍图类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseDiagram baseDiagram)
    {
        List<BaseDiagram> list = baseDiagramService.selectBaseDiagramList(baseDiagram);
        ExcelUtil<BaseDiagram> util = new ExcelUtil<BaseDiagram>(BaseDiagram.class);
        util.exportExcel(response, list, "介绍图类别数据");
    }

    /**
     * 获取介绍图类别详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:diagramType:query')")
    @GetMapping(value = "/{diagramType}")
    public AjaxResult getInfo(@PathVariable("diagramType") String diagramType)
    {
        return AjaxResult.success(baseDiagramService.selectBaseDiagramByDiagramType(diagramType));
    }

    /**
     * 新增介绍图类别
     */
    @PreAuthorize("@ss.hasPermi('business:diagramType:add')")
    @Log(title = "介绍图类别", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseDiagram baseDiagram)
    {
        return toAjax(baseDiagramService.insertBaseDiagram(baseDiagram));
    }

    /**
     * 修改介绍图类别
     */
    @PreAuthorize("@ss.hasPermi('business:diagramType:edit')")
    @Log(title = "介绍图类别", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseDiagram baseDiagram)
    {
        return toAjax(baseDiagramService.updateBaseDiagram(baseDiagram));
    }

    /**
     * 删除介绍图类别
     */
    @PreAuthorize("@ss.hasPermi('business:diagramType:remove')")
    @Log(title = "介绍图类别", businessType = BusinessType.DELETE)
	@DeleteMapping("/{diagramTypes}")
    public AjaxResult remove(@PathVariable String[] diagramTypes)
    {
        return toAjax(baseDiagramService.deleteBaseDiagramByDiagramTypes(diagramTypes));
    }
}
