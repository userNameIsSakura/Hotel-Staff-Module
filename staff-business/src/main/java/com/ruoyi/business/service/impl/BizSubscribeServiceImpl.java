package com.ruoyi.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.SubscribeParam;
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
        final BizSubscribe bizSubscribe = bizSubscribeMapper.selectBizSubscribeBySubscribeId(subscribeId);
//        final HashMap hashMap = JSONObject.parseObject(bizSubscribe.getParameter());
        return bizSubscribe;
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
        // TODO: 2023/6/14 新增限制参数
        System.out.println(bizSubscribe);
        final ArrayList<SubscribeParam> paramList = bizSubscribe.getParamList();
        if(paramList == null || paramList.size() == 0) {
            bizSubscribe.setParameter("");
        }
        final HashMap<String, Object> map = new HashMap<>();
        for (SubscribeParam subscribeParam : paramList) {
            map.put(subscribeParam.getKey(),subscribeParam.getValue());
        }
        bizSubscribe.setParameter(JSONObject.toJSONString(map));

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
        System.out.println(bizSubscribe);
        final ArrayList<SubscribeParam> paramList = bizSubscribe.getParamList();
        if(paramList == null || paramList.size() == 0) {
            bizSubscribe.setParameter("");
        }
        final HashMap<String, Object> map = new HashMap<>();
        for (SubscribeParam subscribeParam : paramList) {
            map.put(subscribeParam.getKey(),subscribeParam.getValue());
        }
        bizSubscribe.setParameter(JSONObject.toJSONString(map));

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
