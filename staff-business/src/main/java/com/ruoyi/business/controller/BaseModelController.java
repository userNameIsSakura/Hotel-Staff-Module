package com.ruoyi.business.controller;

import com.ruoyi.business.domain.BaseModel;
import com.ruoyi.business.service.IBaseModelService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 范本列表Controller
 *
 * @author ruoyi
 * @date 2022-12-03
 */
@RestController
@RequestMapping("/business/model")
public class BaseModelController extends BaseController
{
    @Autowired
    private IBaseModelService baseModelService;

    /**
     * 查询范本列表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BaseModel baseModel)
    {
        startPage();
        List<BaseModel> list = baseModelService.selectBaseModelList(baseModel);
        return getDataTable(list);
    }

    /**
     * 导出范本列表列表
     */
    @Log(title = "范本列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseModel baseModel)
    {
        List<BaseModel> list = baseModelService.selectBaseModelList(baseModel);
        ExcelUtil<BaseModel> util = new ExcelUtil<BaseModel>(BaseModel.class);
        util.exportExcel(response, list, "范本列表数据");
    }

    /**
     * 获取范本列表详细信息
     */
    @GetMapping(value = "/{modelId}")
    public AjaxResult getInfo(@PathVariable("modelId") Long modelId)
    {
        return AjaxResult.success(baseModelService.selectBaseModelByModelId(modelId));
    }

    /**
     * 新增范本列表
     */
    @Log(title = "范本列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseModel baseModel)
    {
        return toAjax(baseModelService.insertBaseModel(baseModel));
    }

    /**
     * 修改范本列表
     */
    @Log(title = "范本列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseModel baseModel)
    {
        return toAjax(baseModelService.updateBaseModel(baseModel));
    }

    /**
     * 删除范本列表
     */
    @Log(title = "范本列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{modelIds}")
    public AjaxResult remove(@PathVariable Long[] modelIds)
    {
        return toAjax(baseModelService.deleteBaseModelByModelIds(modelIds));
    }
}
