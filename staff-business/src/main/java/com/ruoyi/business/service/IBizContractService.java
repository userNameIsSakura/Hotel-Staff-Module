package com.ruoyi.business.service;

import com.ruoyi.business.domain.BizContract;

import java.util.List;

/**
 * 合同记录Service接口
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public interface IBizContractService
{
    /**
     * 查询合同记录
     *
     * @param contractId 合同记录主键
     * @return 合同记录
     */
    public BizContract selectBizContractByContractId(Long contractId);

    /**
     * 查询合同记录列表
     *
     * @param bizContract 合同记录
     * @return 合同记录集合
     */
    public List<BizContract> selectBizContractList(BizContract bizContract);

    /**
     * 新增合同记录
     *
     * @param bizContract 合同记录
     * @return 结果
     */
    public int insertBizContract(BizContract bizContract);

    /**
     * 修改合同记录
     *
     * @param bizContract 合同记录
     * @return 结果
     */
    public int updateBizContract(BizContract bizContract);

    /**
     * 批量删除合同记录
     *
     * @param contractIds 需要删除的合同记录主键集合
     * @return 结果
     */
    public int deleteBizContractByContractIds(Long[] contractIds);

    /**
     * 删除合同记录信息
     *
     * @param contractId 合同记录主键
     * @return 结果
     */
    public int deleteBizContractByContractId(Long contractId);
}
