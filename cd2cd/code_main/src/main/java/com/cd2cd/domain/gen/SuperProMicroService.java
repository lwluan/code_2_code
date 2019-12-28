package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProMicroService implements Serializable {
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务介绍
     */
    private String description;

    /**
     * 版本号
     */
    private String version;

    /**
     * com.test
     */
    private String groupId;

    private String artifactId;

    /**
     * 访问路径
     */
    private String contextPath;

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
     * 服务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 服务名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 服务介绍
     */
    public String getDescription() {
        return description;
    }

    /**
     * 服务介绍
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 版本号
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * com.test
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * com.test
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    /**
     * 访问路径
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * 访问路径
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
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
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", version=").append(version);
        sb.append(", groupId=").append(groupId);
        sb.append(", artifactId=").append(artifactId);
        sb.append(", contextPath=").append(contextPath);
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
        SuperProMicroService other = (SuperProMicroService) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getArtifactId() == null ? other.getArtifactId() == null : this.getArtifactId().equals(other.getArtifactId()))
            && (this.getContextPath() == null ? other.getContextPath() == null : this.getContextPath().equals(other.getContextPath()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getArtifactId() == null) ? 0 : getArtifactId().hashCode());
        result = prime * result + ((getContextPath() == null) ? 0 : getContextPath().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}