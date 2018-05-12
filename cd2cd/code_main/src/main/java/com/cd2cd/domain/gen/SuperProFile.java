package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProFile implements Serializable {
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 模块ID
     */
    private Long moduleId;

    /**
     * 父类ID
     */
    private Long superId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型：controller|service|vo|dao|domain|
     */
    private String type;

    /**
     * 类类型：class|generics|enum|interface|abstruct
     */
    private String dtype;

    /**
     * 是否为范型:no\yes
     */
    private String paradigm;

    /**
     * 类类型:class、enum、interface、abstruct
     */
    private String classType;

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
     * 父类ID
     */
    public Long getSuperId() {
        return superId;
    }

    /**
     * 父类ID
     */
    public void setSuperId(Long superId) {
        this.superId = superId;
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
     * 文件类型：controller|service|vo|dao|domain|
     */
    public String getType() {
        return type;
    }

    /**
     * 文件类型：controller|service|vo|dao|domain|
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

    /**
     * 是否为范型:no\yes
     */
    public String getParadigm() {
        return paradigm;
    }

    /**
     * 是否为范型:no\yes
     */
    public void setParadigm(String paradigm) {
        this.paradigm = paradigm;
    }

    /**
     * 类类型:class、enum、interface、abstruct
     */
    public String getClassType() {
        return classType;
    }

    /**
     * 类类型:class、enum、interface、abstruct
     */
    public void setClassType(String classType) {
        this.classType = classType;
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
        sb.append(", moduleId=").append(moduleId);
        sb.append(", superId=").append(superId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", dtype=").append(dtype);
        sb.append(", paradigm=").append(paradigm);
        sb.append(", classType=").append(classType);
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
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getModuleId() == null ? other.getModuleId() == null : this.getModuleId().equals(other.getModuleId()))
            && (this.getSuperId() == null ? other.getSuperId() == null : this.getSuperId().equals(other.getSuperId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDtype() == null ? other.getDtype() == null : this.getDtype().equals(other.getDtype()))
            && (this.getParadigm() == null ? other.getParadigm() == null : this.getParadigm().equals(other.getParadigm()))
            && (this.getClassType() == null ? other.getClassType() == null : this.getClassType().equals(other.getClassType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getModuleId() == null) ? 0 : getModuleId().hashCode());
        result = prime * result + ((getSuperId() == null) ? 0 : getSuperId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getDtype() == null) ? 0 : getDtype().hashCode());
        result = prime * result + ((getParadigm() == null) ? 0 : getParadigm().hashCode());
        result = prime * result + ((getClassType() == null) ? 0 : getClassType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}