package com.ruoyi.business.service;

import com.ruoyi.business.domain.SysOperationLog;
import java.util.List;

/**
 * (SysOperationLog)表服务接口
 *
 * @author makejava
 * @since 2023-05-10 16:25:23
 */
public interface SysOperationLogService {

    /**
     * 查询所有数据
     *
     * @return 实例对象集合
     */
    List<SysOperationLog> queryAll();

    /**
     * 通过ID查询单条数据
     *
     * @param logId 主键
     * @return 实例对象
     */
    SysOperationLog queryById(Integer logId);

    /**
     * 新增数据
     *
     * @param sysOperationLog 实例对象
     * @return 实例对象
     */
    SysOperationLog insert(SysOperationLog sysOperationLog);

    /**
     * 修改数据
     *
     * @param sysOperationLog 实例对象
     * @return 实例对象
     */
    SysOperationLog update(SysOperationLog sysOperationLog);

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer logId);

}

