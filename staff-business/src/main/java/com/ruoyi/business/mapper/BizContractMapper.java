package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.BizContract;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;

import java.util.List;

/**
 * 合同记录Mapper接口
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public interface BizContractMapper
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
     * 删除合同记录
     *
     * @param contractId 合同记录主键
     * @return 结果
     */
    public int deleteBizContractByContractId(Long contractId);

    /**
     * 批量删除合同记录
     *
     * @param contractIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizContractByContractIds(Long[] contractIds);
}
