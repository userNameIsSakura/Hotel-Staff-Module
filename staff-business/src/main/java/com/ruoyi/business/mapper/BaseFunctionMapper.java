package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BaseFunction;

/**
 * 职能信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
public interface BaseFunctionMapper 
{
    /**
     * 查询职能信息
     * 
     * @param functionId 职能信息主键
     * @return 职能信息
     */
    public BaseFunction selectBaseFunctionByFunctionId(Long functionId);

    /**
     * 查询职能信息列表
     * 
     * @param baseFunction 职能信息
     * @return 职能信息集合
     */
    public List<BaseFunction> selectBaseFunctionList(BaseFunction baseFunction);

    /**
     * 新增职能信息
     * 
     * @param baseFunction 职能信息
     * @return 结果
     */
    public int insertBaseFunction(BaseFunction baseFunction);

    /**
     * 修改职能信息
     * 
     * @param baseFunction 职能信息
     * @return 结果
     */
    public int updateBaseFunction(BaseFunction baseFunction);

    /**
     * 删除职能信息
     * 
     * @param functionId 职能信息主键
     * @return 结果
     */
    public int deleteBaseFunctionByFunctionId(Long functionId);

    /**
     * 批量删除职能信息
     * 
     * @param functionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseFunctionByFunctionIds(Long[] functionIds);
}
