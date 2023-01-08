package com.ruoyi.business.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.business.mapper.BasePositionMapper;
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
import com.ruoyi.business.domain.BasePosition;
import com.ruoyi.business.service.IBasePositionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 职位信息Controller
 *
 * @author ruoyi
 * @date 2022-11-18
 */
@RestController
@RequestMapping("/business/position")
public class BasePositionController extends BaseController
{
    @Autowired
    private IBasePositionService basePositionService;
    @Autowired
    private BasePositionMapper basePositionMapper;

    /**
     * 查询职位信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:position:list')")
    @GetMapping("/list")
    public TableDataInfo list(BasePosition basePosition)
    {
        startPage();
        basePosition.setHotelId(SecurityUtils.getHotelId());
        List<BasePosition> list = basePositionService.selectBasePositionList(basePosition);
        return getDataTable(list);
    }
    /**
     * 查询职位信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:position:list')")
    @GetMapping("/listAll")
    public List<BasePosition> list()
    {
        BasePosition basePosition = new BasePosition();
        basePosition.setHotelId(SecurityUtils.getHotelId());
        return basePositionService.selectBasePositionList(basePosition);
    }

    /**
     * 查询职位信息列表
     */
    @GetMapping("/listByDId/{departmentId}")
    public List<BasePosition> listByDId(@PathVariable Long departmentId)
    {
        System.out.println(departmentId);
        return basePositionMapper.selectBasePositionByDepartmentId(departmentId);
    }




    /**
     * 导出职位信息列表
     */
/*
    @PreAuthorize("@ss.hasPermi('business:position:export')")
    @Log(title = "职位信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BasePosition basePosition)
    {
        List<BasePosition> list = basePositionService.selectBasePositionList(basePosition);
        ExcelUtil<BasePosition> util = new ExcelUtil<BasePosition>(BasePosition.class);
        util.exportExcel(response, list, "职位信息数据");
    }
*/

    /**
     * 获取职位信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:position:query')")
    @GetMapping(value = "/{positionId}")
    public AjaxResult getInfo(@PathVariable("positionId") Long positionId)
    {
        return AjaxResult.success(basePositionService.selectBasePositionByPositionId(positionId));
    }

    /**
     * 新增职位信息
     */
    @PreAuthorize("@ss.hasPermi('business:position:add')")
    @Log(title = "职位信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BasePosition basePosition)
    {
        basePosition.setHotelId(SecurityUtils.getHotelId());
        return toAjax(basePositionService.insertBasePosition(basePosition));
    }

    /**
     * 修改职位信息
     */
    @PreAuthorize("@ss.hasPermi('business:position:edit')")
    @Log(title = "职位信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BasePosition basePosition)
    {
        basePosition.setHotelId(SecurityUtils.getHotelId());
        return toAjax(basePositionService.updateBasePosition(basePosition));
    }

    /**
     * 删除职位信息
     */
    @PreAuthorize("@ss.hasPermi('business:position:remove')")
    @Log(title = "职位信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{positionIds}")
    public AjaxResult remove(@PathVariable Long[] positionIds)
    {
        return toAjax(basePositionService.deleteBasePositionByPositionIds(positionIds));
    }
}
