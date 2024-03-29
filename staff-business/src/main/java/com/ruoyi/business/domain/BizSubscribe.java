package com.ruoyi.business.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;

/**
 * 订阅信息对象 biz_subscribe
 *
 * @author ruoyi
 * @date 2023-01-30
 */
public class BizSubscribe extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订阅ID */
    private Long subscribeId;

    /** 订阅内容 */
    @Excel(name = "订阅内容")
    private String subscribeContent;

    /** 限制参数 */
    @Excel(name = "限制参数")
    private String parameter;

    /** 是否可用 */
    @Excel(name = "是否可用")
    private Integer available;

    private ArrayList<SubscribeParam> paramList;

    public ArrayList<SubscribeParam> getParamList() {
        return paramList;
    }

    public void setParamList(ArrayList<SubscribeParam> paramList) {
        this.paramList = paramList;
    }

    public void setSubscribeId(Long subscribeId)
    {
        this.subscribeId = subscribeId;
    }

    public Long getSubscribeId()
    {
        return subscribeId;
    }
    public void setSubscribeContent(String subscribeContent)
    {
        this.subscribeContent = subscribeContent;
    }

    public String getSubscribeContent()
    {
        return subscribeContent;
    }
    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    public String getParameter()
    {
        return parameter;
    }
    public void setAvailable(Integer available)
    {
        this.available = available;
    }

    public Integer getAvailable()
    {
        return available;
    }

    @Override
    public String toString() {
        return "BizSubscribe{" +
                "subscribeId=" + subscribeId +
                ", subscribeContent='" + subscribeContent + '\'' +
                ", parameter='" + parameter + '\'' +
                ", available=" + available +
                ", paramList=" + paramList +
                '}';
    }
}
