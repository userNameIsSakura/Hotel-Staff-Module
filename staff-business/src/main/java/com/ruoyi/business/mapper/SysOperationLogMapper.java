package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.SysOperationLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * (SysOperationLog)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-10 16:25:23
 */
@Mapper
public interface SysOperationLogMapper {

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
     * 统计总行数
     *
     * @param sysOperationLog 查询条件
     * @return 总行数
     */
    long count(SysOperationLog sysOperationLog);

    /**
     * 新增数据
     *
     * @param sysOperationLog 实例对象
     * @return 影响行数
     */
    int insert(SysOperationLog sysOperationLog);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysOperationLog> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SysOperationLog> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysOperationLog> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SysOperationLog> entities);

    /**
     * 修改数据
     *
     * @param sysOperationLog 实例对象
     * @return 影响行数
     */
    int update(SysOperationLog sysOperationLog);

    /**
     * 通过主键删除数据
     *
     * @param logId 主键
     * @return 影响行数
     */
    int deleteById(Integer logId);

}


