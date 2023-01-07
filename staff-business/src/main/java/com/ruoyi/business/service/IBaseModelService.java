package com.ruoyi.business.service;

import com.ruoyi.business.domain.BaseModel;

import java.util.List;

/**
 * 范本列表Service接口
 *
 * @author ruoyi
 * @date 2022-12-03
 */
public interface IBaseModelService
{
    /**
     * 查询范本列表
     *
     * @param modelId 范本列表主键
     * @return 范本列表
     */
    public BaseModel selectBaseModelByModelId(Long modelId);

    /**
     * 查询范本列表列表
     *
     * @param baseModel 范本列表
     * @return 范本列表集合
     */
    public List<BaseModel> selectBaseModelList(BaseModel baseModel);

    /**
     * 新增范本列表
     *
     * @param baseModel 范本列表
     * @return 结果
     */
    public int insertBaseModel(BaseModel baseModel);

    /**
     * 修改范本列表
     *
     * @param baseModel 范本列表
     * @return 结果
     */
    public int updateBaseModel(BaseModel baseModel);

    /**
     * 批量删除范本列表
     *
     * @param modelIds 需要删除的范本列表主键集合
     * @return 结果
     */
    public int deleteBaseModelByModelIds(Long[] modelIds);

    /**
     * 删除范本列表信息
     *
     * @param modelId 范本列表主键
     * @return 结果
     */
    public int deleteBaseModelByModelId(Long modelId);
}
