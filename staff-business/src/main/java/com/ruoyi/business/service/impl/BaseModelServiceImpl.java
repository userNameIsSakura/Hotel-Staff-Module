package com.ruoyi.business.service.impl;

import com.ruoyi.business.domain.BaseModel;
import com.ruoyi.business.mapper.BaseModelMapper;
import com.ruoyi.business.service.IBaseModelService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 范本列表Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-03
 */
@Service
public class BaseModelServiceImpl implements IBaseModelService
{
    @Autowired
    private BaseModelMapper baseModelMapper;

    /**
     * 查询范本列表
     *
     * @param modelId 范本列表主键
     * @return 范本列表
     */
    @Override
    public BaseModel selectBaseModelByModelId(Long modelId)
    {
        return baseModelMapper.selectBaseModelByModelId(modelId);
    }

    /**
     * 查询范本列表列表
     *
     * @param baseModel 范本列表
     * @return 范本列表
     */
    @Override
    public List<BaseModel> selectBaseModelList(BaseModel baseModel)
    {
        return baseModelMapper.selectBaseModelList(baseModel);
    }

    /**
     * 新增范本列表
     *
     * @param baseModel 范本列表
     * @return 结果
     */
    @Override
    public int insertBaseModel(BaseModel baseModel)
    {
        return baseModelMapper.insertBaseModel(baseModel);
    }

    /**
     * 修改范本列表
     *
     * @param baseModel 范本列表
     * @return 结果
     */
    @Override
    public int updateBaseModel(BaseModel baseModel)
    {
        return baseModelMapper.updateBaseModel(baseModel);
    }

    /**
     * 批量删除范本列表
     *
     * @param modelIds 需要删除的范本列表主键
     * @return 结果
     */
    @Override
    public int deleteBaseModelByModelIds(Long[] modelIds)
    {
        return baseModelMapper.deleteBaseModelByModelIds(modelIds);
    }

    /**
     * 删除范本列表信息
     *
     * @param modelId 范本列表主键
     * @return 结果
     */
    @Override
    public int deleteBaseModelByModelId(Long modelId)
    {
        return baseModelMapper.deleteBaseModelByModelId(modelId);
    }
}
