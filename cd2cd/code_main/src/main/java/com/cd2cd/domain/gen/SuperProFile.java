package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProFile implements Serializable {
    private Long id;

    /**
     * 模块ID
     */
    private Long moduleId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型：vo|dao|service|controller|domain
     */
    private String type;

    /**
     * 类类型：class|generics|enum|interface|abstruct
     */
    private String dtype;

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
     * 模块ID
     */
    public Long getModuleId() {
        return moduleId;
    }

    /**
     * 模块ID
     */
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 文件名称
     */
    public String getName() {
        return name;
    }

    /**
     * 文件名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 文件类型：vo|dao|service|controller|domain
     */
    public String getType() {
        return type;
    }

    /**
     * 文件类型：vo|dao|service|controller|domain
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 类类型：class|generics|enum|interface|abstruct
     */
    public String getDtype() {
        return dtype;
    }

    /**
     * 类类型：class|generics|enum|interface|abstruct
     */
    public void setDtype(String dtype) {
        this.dtype = dtype;
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
        sb.append(", moduleId=").append(moduleId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", dtype=").append(dtype);
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
        SuperProFile other = (SuperProFile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getModuleId() == null ? other.getModuleId() == null : this.getModuleId().equals(other.getModuleId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDtype() == null ? other.getDtype() == null : this.getDtype().equals(other.getDtype()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDtype() == null) ? 0 : getDtype().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}