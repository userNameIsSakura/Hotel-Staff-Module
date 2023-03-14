package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BaseDiagram;

/**
 * 介绍图类别Mapper接口
 * 
 * @author ruoyi
 * @date 2023-03-14
 */
public interface BaseDiagramMapper 
{
    /**
     * 查询介绍图类别
     * 
     * @param diagramType 介绍图类别主键
     * @return 介绍图类别
     */
    public BaseDiagram selectBaseDiagramByDiagramType(String diagramType);

    /**
     * 查询介绍图类别列表
     * 
     * @param baseDiagram 介绍图类别
     * @return 介绍图类别集合
     */
    public List<BaseDiagram> selectBaseDiagramList(BaseDiagram baseDiagram);

    /**
     * 新增介绍图类别
     * 
     * @param baseDiagram 介绍图类别
     * @return 结果
     */
    public int insertBaseDiagram(BaseDiagram baseDiagram);

    /**
     * 修改介绍图类别
     * 
     * @param baseDiagram 介绍图类别
     * @return 结果
     */
    public int updateBaseDiagram(BaseDiagram baseDiagram);

    /**
     * 删除介绍图类别
     * 
     * @param diagramType 介绍图类别主键
     * @return 结果
     */
    public int deleteBaseDiagramByDiagramType(String diagramType);

    /**
     * 批量删除介绍图类别
     * 
     * @param diagramTypes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseDiagramByDiagramTypes(String[] diagramTypes);
}
