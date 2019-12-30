package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProModule implements Serializable {
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 微服务ID
     */
    private Long microId;

    /**
     * 程序名称
     */
    private String name;

    /**
     * 显示名称
     */
    private String showName;

    /**
     * 描述
     */
    private String description;

    /**
     * 1删除；0正常
     */
    private Integer delFlag;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 项目ID
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * 项目ID
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * 微服务ID
     */
    public Long getMicroId() {
        return microId;
    }

    /**
     * 微服务ID
     */
    public void setMicroId(Long microId) {
        this.microId = microId;
    }

    /**
     * 程序名称
     */
    public String getName() {
        return name;
    }

    /**
     * 程序名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 显示名称
     */
    public String getShowName() {
        return showName;
    }

    /**
     * 显示名称
     */
    public void setShowName(String showName) {
        this.showName = showName;
    }

    /**
     * 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 1删除；0正常
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 1删除；0正常
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", projectId=").append(projectId);
        sb.append(", microId=").append(microId);
        sb.append(", name=").append(name);
        sb.append(", showName=").append(showName);
        sb.append(", description=").append(description);
        sb.append(", delFlag=").append(delFlag);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SuperProModule other = (SuperProModule) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getMicroId() == null ? other.getMicroId() == null : this.getMicroId().equals(other.getMicroId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getShowName() == null ? other.getShowName() == null : this.getShowName().equals(other.getShowName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getMicroId() == null) ? 0 : getMicroId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getShowName() == null) ? 0 : getShowName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}