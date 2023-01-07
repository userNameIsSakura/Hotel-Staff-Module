package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.BaseAuthMapper;
import com.ruoyi.business.domain.BaseAuth;
import com.ruoyi.business.service.IBaseAuthService;

/**
 * 权限信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-01-07
 */
@Service
public class BaseAuthServiceImpl implements IBaseAuthService
{
    @Autowired
    private BaseAuthMapper baseAuthMapper;

    @Override
    public List<BaseAuth> selectBaseAuthByRoleId(Long roleId) {
        return baseAuthMapper.selectBaseAuthByRoleId(roleId);
    }

    /**
     * 查询权限信息
     *
     * @param authId 权限信息主键
     * @return 权限信息
     */
    @Override
    public BaseAuth selectBaseAuthByAuthId(Long authId)
    {
        return baseAuthMapper.selectBaseAuthByAuthId(authId);
    }

    /**
     * 查询权限信息列表
     *
     * @param baseAuth 权限信息
     * @return 权限信息
     */
    @Override
    public List<BaseAuth> selectBaseAuthList(BaseAuth baseAuth)
    {
        return baseAuthMapper.selectBaseAuthList(baseAuth);
    }

    /**
     * 新增权限信息
     *
     * @param baseAuth 权限信息
     * @return 结果
     */
    @Override
    public int insertBaseAuth(BaseAuth baseAuth)
    {
        return baseAuthMapper.insertBaseAuth(baseAuth);
    }

    /**
     * 修改权限信息
     *
     * @param baseAuth 权限信息
     * @return 结果
     */
    @Override
    public int updateBaseAuth(BaseAuth baseAuth)
    {
        return baseAuthMapper.updateBaseAuth(baseAuth);
    }

    /**
     * 批量删除权限信息
     *
     * @param authIds 需要删除的权限信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseAuthByAuthIds(Long[] authIds)
    {
        return baseAuthMapper.deleteBaseAuthByAuthIds(authIds);
    }

    /**
     * 删除权限信息信息
     *
     * @param authId 权限信息主键
     * @return 结果
     */
    @Override
    public int deleteBaseAuthByAuthId(Long authId)
    {
        return baseAuthMapper.deleteBaseAuthByAuthId(authId);
    }
}
