package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseFunctionMapper;
import com.ruoyi.business.domain.BaseFunction;
import com.ruoyi.business.service.IBaseFunctionService;

/**
 * 职能信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-18
 */
@Service
public class BaseFunctionServiceImpl implements IBaseFunctionService 
{
    @Autowired
    private BaseFunctionMapper baseFunctionMapper;

    /**
     * 查询职能信息
     * 
     * @param functionId 职能信息主键
     * @return 职能信息
     */
    @Override
    public BaseFunction selectBaseFunctionByFunctionId(Long functionId)
    {
        return baseFunctionMapper.selectBaseFunctionByFunctionId(functionId);
    }

    /**
     * 查询职能信息列表
     * 
     * @param baseFunction 职能信息
     * @return 职能信息
     */
    @Override
    public List<BaseFunction> selectBaseFunctionList(BaseFunction baseFunction)
    {
        return baseFunctionMapper.selectBaseFunctionList(baseFunction);
    }

    /**
     * 新增职能信息
     * 
     * @param baseFunction 职能信息
     * @return 结果
     */
    @Override
    public int insertBaseFunction(BaseFunction baseFunction)
    {
        return baseFunctionMapper.insertBaseFunction(baseFunction);
    }

    /**
     * 修改职能信息
     * 
     * @param baseFunction 职能信息
     * @return 结果
     */
    @Override
    public int updateBaseFunction(BaseFunction baseFunction)
    {
        return baseFunctionMapper.updateBaseFunction(baseFunction);
    }

    /**
     * 批量删除职能信息
     * 
     * @param functionIds 需要删除的职能信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseFunctionByFunctionIds(Long[] functionIds)
    {
        return baseFunctionMapper.deleteBaseFunctionByFunctionIds(functionIds);
    }

    /**
     * 删除职能信息信息
     * 
     * @param functionId 职能信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseFunctionByFunctionId(Long functionId)
    {
        return baseFunctionMapper.deleteBaseFunctionByFunctionId(functionId);
    }
}
