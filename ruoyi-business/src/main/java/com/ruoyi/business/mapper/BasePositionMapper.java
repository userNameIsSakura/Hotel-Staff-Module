package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BasePosition;

/**
 * 职位信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-18
 */
public interface BasePositionMapper
{
    /**
     * 查询职位信息
     *
     * @param positionId 职位信息主键
     * @return 职位信息
     */
    public BasePosition selectBasePositionByPositionId(Long positionId);
    /**
     * 查询职位信息
     *
     */
    public List<BasePosition> selectBasePositionByDepartmentId(Long departmentId);

    /**
     * 查询职位信息列表
     *
     * @param basePosition 职位信息
     * @return 职位信息集合
     */
    public List<BasePosition> selectBasePositionList(BasePosition basePosition);

    /**
     * 新增职位信息
     *
     * @param basePosition 职位信息
     * @return 结果
     */
    public int insertBasePosition(BasePosition basePosition);

    /**
     * 修改职位信息
     *
     * @param basePosition 职位信息
     * @return 结果
     */
    public int updateBasePosition(BasePosition basePosition);

    /**
     * 删除职位信息
     *
     * @param positionId 职位信息主键
     * @return 结果
     */
    public int deleteBasePositionByPositionId(Long positionId);

    /**
     * 批量删除职位信息
     *
     * @param positionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBasePositionByPositionIds(Long[] positionIds);
}
