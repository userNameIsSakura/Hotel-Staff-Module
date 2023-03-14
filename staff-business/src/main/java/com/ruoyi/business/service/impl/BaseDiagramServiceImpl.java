package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseDiagramMapper;
import com.ruoyi.business.domain.BaseDiagram;
import com.ruoyi.business.service.IBaseDiagramService;

/**
 * 介绍图类别Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-14
 */
@Service
public class BaseDiagramServiceImpl implements IBaseDiagramService 
{
    @Autowired
    private BaseDiagramMapper baseDiagramMapper;

    /**
     * 查询介绍图类别
     * 
     * @param diagramType 介绍图类别主键
     * @return 介绍图类别
     */
    @Override
    public BaseDiagram selectBaseDiagramByDiagramType(String diagramType)
    {
        return baseDiagramMapper.selectBaseDiagramByDiagramType(diagramType);
    }

    /**
     * 查询介绍图类别列表
     * 
     * @param baseDiagram 介绍图类别
     * @return 介绍图类别
     */
    @Override
    public List<BaseDiagram> selectBaseDiagramList(BaseDiagram baseDiagram)
    {
        return baseDiagramMapper.selectBaseDiagramList(baseDiagram);
    }

    /**
     * 新增介绍图类别
     * 
     * @param baseDiagram 介绍图类别
     * @return 结果
     */
    @Override
    public int insertBaseDiagram(BaseDiagram baseDiagram)
    {
        return baseDiagramMapper.insertBaseDiagram(baseDiagram);
    }

    /**
     * 修改介绍图类别
     * 
     * @param baseDiagram 介绍图类别
     * @return 结果
     */
    @Override
    public int updateBaseDiagram(BaseDiagram baseDiagram)
    {
        return baseDiagramMapper.updateBaseDiagram(baseDiagram);
    }

    /**
     * 批量删除介绍图类别
     * 
     * @param diagramTypes 需要删除的介绍图类别主键
     * @return 结果
     */
    @Override
    public int deleteBaseDiagramByDiagramTypes(String[] diagramTypes)
    {
        return baseDiagramMapper.deleteBaseDiagramByDiagramTypes(diagramTypes);
    }

    /**
     * 删除介绍图类别信息
     * 
     * @param diagramType 介绍图类别主键
     * @return 结果
     */
    @Override
    public int deleteBaseDiagramByDiagramType(String diagramType)
    {
        return baseDiagramMapper.deleteBaseDiagramByDiagramType(diagramType);
    }
}
