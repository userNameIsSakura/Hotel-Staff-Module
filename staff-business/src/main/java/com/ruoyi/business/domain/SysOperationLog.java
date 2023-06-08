package com.ruoyi.business.domain;

import java.util.Date;
import java.io.Serializable;

/**
 * (SysOperationLog)实体类
 *
 * @author makejava
 * @since 2023-05-10 16:25:23
 */
public class SysOperationLog implements Serializable {
    private static final long serialVersionUID = 879518558771962740L;

    private Integer logId;

    private Long logUser;

    private String logCommand;

    private Date logTime;

    private String logData;

    private Integer logType;

    private String logTarget;


    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Long getLogUser() {
        return logUser;
    }

    public void setLogUser(Long logUser) {
        this.logUser = logUser;
    }

    public String getLogCommand() {
        return logCommand;
    }

    public void setLogCommand(String logCommand) {
        this.logCommand = logCommand;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogTarget() {
        return logTarget;
    }

    public void setLogTarget(String logTarget) {
        this.logTarget = logTarget;
    }

}


