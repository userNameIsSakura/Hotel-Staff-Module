package com.ruoyi.business.service.impl;

import com.ruoyi.business.domain.SysOperationLog;
import com.ruoyi.business.mapper.SysOperationLogMapper;
import com.ruoyi.business.service.SysOperationLogService;
import org.springframework.stereotype.Service;
import java.util.List;
import javax.annotation.Resource;

/**
 * (SysOperationLog)表服务实现类
 *
 * @author makejava
 * @since 2023-05-10 16:25:22
 */
@Service("sysOperationLogService")
public class SysOperationLogServiceImpl implements SysOperationLogService {
    @Resource
    private SysOperationLogMapper sysOperationLogMapper;

    /**
     * 查询所有数据
     *
     * @return 实例对象集合
     */
    @Override
    public List<SysOperationLog> queryAll() {
        return this.sysOperationLogMapper.queryAll();
    }

    /**
     * 通过ID查询单条数据
     *
     * @param logId 主键
     * @return 实例对象
     */
    @Override
    public SysOperationLog queryById(Integer logId) {
        return this.sysOperationLogMapper.queryById(logId);
    }

    /**
     * 新增数据
     *
     * @param sysOperationLog 实例对象
     * @return 实例对象
     */
    @Override
    public SysOperationLog insert(SysOperationLog sysOperationLog) {
        this.sysOperationLogMapper.insert(sysOperationLog);
        return sysOperationLog;
    }

    /**
     * 修改数据
     *
     * @param sysOperationLog 实例对象
     * @return 实例对象
     */
    @Override
    public SysOperationLog update(SysOperationLog sysOperationLog) {
        this.sysOperationLogMapper.update(sysOperationLog);
        return this.queryById(sysOperationLog.getLogId());
    }

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer logId) {
        return this.sysOperationLogMapper.deleteById(logId) > 0;
    }
}

