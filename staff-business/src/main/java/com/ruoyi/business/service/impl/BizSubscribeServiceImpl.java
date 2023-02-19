package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BizSubscribeMapper;
import com.ruoyi.business.domain.BizSubscribe;
import com.ruoyi.business.service.IBizSubscribeService;

/**
 * 订阅信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-01-30
 */
@Service
public class BizSubscribeServiceImpl implements IBizSubscribeService 
{
    @Autowired
    private BizSubscribeMapper bizSubscribeMapper;

    /**
     * 查询订阅信息
     * 
     * @param subscribeId 订阅信息主键
     * @return 订阅信息
     */
    @Override
    public BizSubscribe selectBizSubscribeBySubscribeId(Long subscribeId)
    {
        return bizSubscribeMapper.selectBizSubscribeBySubscribeId(subscribeId);
    }

    /**
     * 查询订阅信息列表
     * 
     * @param bizSubscribe 订阅信息
     * @return 订阅信息
     */
    @Override
    public List<BizSubscribe> selectBizSubscribeList(BizSubscribe bizSubscribe)
    {
        return bizSubscribeMapper.selectBizSubscribeList(bizSubscribe);
    }

    /**
     * 新增订阅信息
     * 
     * @param bizSubscribe 订阅信息
     * @return 结果
     */
    @Override
    public int insertBizSubscribe(BizSubscribe bizSubscribe)
    {
        return bizSubscribeMapper.insertBizSubscribe(bizSubscribe);
    }

    /**
     * 修改订阅信息
     * 
     * @param bizSubscribe 订阅信息
     * @return 结果
     */
    @Override
    public int updateBizSubscribe(BizSubscribe bizSubscribe)
    {
        return bizSubscribeMapper.updateBizSubscribe(bizSubscribe);
    }

    /**
     * 批量删除订阅信息
     * 
     * @param subscribeIds 需要删除的订阅信息主键
     * @return 结果
     */
    @Override
    public int deleteBizSubscribeBySubscribeIds(Long[] subscribeIds)
    {
        return bizSubscribeMapper.deleteBizSubscribeBySubscribeIds(subscribeIds);
    }

    /**
     * 删除订阅信息信息
     * 
     * @param subscribeId 订阅信息主键
     * @return 结果
     */
    @Override
    public int deleteBizSubscribeBySubscribeId(Long subscribeId)
    {
        return bizSubscribeMapper.deleteBizSubscribeBySubscribeId(subscribeId);
    }
}
