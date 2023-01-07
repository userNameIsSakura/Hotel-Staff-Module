package com.ruoyi.business.service.impl;

import com.ruoyi.business.domain.BizContract;
import com.ruoyi.business.mapper.BizContractMapper;
import com.ruoyi.business.service.IBizContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合同记录Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-07
 */
@Service
public class BizContractServiceImpl implements IBizContractService
{
    @Autowired
    private BizContractMapper bizContractMapper;

    /**
     * 查询合同记录
     *
     * @param contractId 合同记录主键
     * @return 合同记录
     */
    @Override
    public BizContract selectBizContractByContractId(Long contractId)
    {
        return bizContractMapper.selectBizContractByContractId(contractId);
    }

    /**
     * 查询合同记录列表
     *
     * @param bizContract 合同记录
     * @return 合同记录
     */
    @Override
    public List<BizContract> selectBizContractList(BizContract bizContract)
    {
        return bizContractMapper.selectBizContractList(bizContract);
    }

    /**
     * 新增合同记录
     *
     * @param bizContract 合同记录
     * @return 结果
     */
    @Override
    public int insertBizContract(BizContract bizContract)
    {
        return bizContractMapper.insertBizContract(bizContract);
    }

    /**
     * 修改合同记录
     *
     * @param bizContract 合同记录
     * @return 结果
     */
    @Override
    public int updateBizContract(BizContract bizContract)
    {
        return bizContractMapper.updateBizContract(bizContract);
    }

    /**
     * 批量删除合同记录
     *
     * @param contractIds 需要删除的合同记录主键
     * @return 结果
     */
    @Override
    public int deleteBizContractByContractIds(Long[] contractIds)
    {
        return bizContractMapper.deleteBizContractByContractIds(contractIds);
    }

    /**
     * 删除合同记录信息
     *
     * @param contractId 合同记录主键
     * @return 结果
     */
    @Override
    public int deleteBizContractByContractId(Long contractId)
    {
        return bizContractMapper.deleteBizContractByContractId(contractId);
    }
}
