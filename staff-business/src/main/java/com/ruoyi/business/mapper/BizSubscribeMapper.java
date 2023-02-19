package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BizSubscribe;

/**
 * 订阅信息Mapper接口
 *
 * @author ruoyi
 * @date 2023-01-30
 */
public interface
BizSubscribeMapper
{
    /**
     * 查询订阅信息
     *
     * @param subscribeId 订阅信息主键
     * @return 订阅信息
     */
    public BizSubscribe selectBizSubscribeBySubscribeId(Long subscribeId);

    /**
     * 查询订阅信息列表
     *
     * @param bizSubscribe 订阅信息
     * @return 订阅信息集合
     */
    public List<BizSubscribe> selectBizSubscribeList(BizSubscribe bizSubscribe);

    /**
     * 新增订阅信息
     *
     * @param bizSubscribe 订阅信息
     * @return 结果
     */
    public int insertBizSubscribe(BizSubscribe bizSubscribe);

    /**
     * 修改订阅信息
     *
     * @param bizSubscribe 订阅信息
     * @return 结果
     */
    public int updateBizSubscribe(BizSubscribe bizSubscribe);

    /**
     * 删除订阅信息
     *
     * @param subscribeId 订阅信息主键
     * @return 结果
     */
    public int deleteBizSubscribeBySubscribeId(Long subscribeId);

    /**
     * 批量删除订阅信息
     *
     * @param subscribeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizSubscribeBySubscribeIds(Long[] subscribeIds);
}
