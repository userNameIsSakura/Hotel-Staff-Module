package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BaseAuth;

/**
 * 权限信息Service接口
 *
 * @author ruoyi
 * @date 2023-01-07
 */
public interface IBaseAuthService
{
    /**
     * 查询权限信息
     *
     * @param authId 权限信息主键
     * @return 权限信息
     */
    public BaseAuth selectBaseAuthByAuthId(Long authId);

    /**
     * 查询权限信息列表
     *
     * @param baseAuth 权限信息
     * @return 权限信息集合
     */
    public List<BaseAuth> selectBaseAuthList(BaseAuth baseAuth);

    /**
     * 新增权限信息
     *
     * @param baseAuth 权限信息
     * @return 结果
     */
    public int insertBaseAuth(BaseAuth baseAuth);

    /**
     * 修改权限信息
     *
     * @param baseAuth 权限信息
     * @return 结果
     */
    public int updateBaseAuth(BaseAuth baseAuth);

    /**
     * 批量删除权限信息
     *
     * @param authIds 需要删除的权限信息主键集合
     * @return 结果
     */
    public int deleteBaseAuthByAuthIds(Long[] authIds);

    /**
     * 删除权限信息信息
     *
     * @param authId 权限信息主键
     * @return 结果
     */
    public int deleteBaseAuthByAuthId(Long authId);

    public List<BaseAuth> selectBaseAuthByRoleId(Long roleId);
}
