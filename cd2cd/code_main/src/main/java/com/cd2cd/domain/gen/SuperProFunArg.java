package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProFunArg implements Serializable {
    private Long id;

    /**
     * 方法ID
     */
    private Long funId;

    /**
     * 参数名称，英文
     */
    private String name;

    /**
     * 参数类型: base、vo 、page
     */
    private String argType;

    /**
     * 集合:single/list/map/set
     */
    private String collectionType;

    /**
     * 备注
     */
    private String comment;

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
     * 方法ID
     */
    public Long getFunId() {
        return funId;
    }

    /**
     * 方法ID
     */
    public void setFunId(Long funId) {
        this.funId = funId;
    }

    /**
     * 参数名称，英文
     */
    public String getName() {
        return name;
    }

    /**
     * 参数名称，英文
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 参数类型: base、vo 、page
     */
    public String getArgType() {
        return argType;
    }

    /**
     * 参数类型: base、vo 、page
     */
    public void setArgType(String argType) {
        this.argType = argType;
    }

    /**
     * 集合:single/list/map/set
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * 集合:single/list/map/set
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 备注
     */
    public void setComment(String comment) {
        this.comment = comment;
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
        sb.append(", funId=").append(funId);
        sb.append(", name=").append(name);
        sb.append(", argType=").append(argType);
        sb.append(", collectionType=").append(collectionType);
        sb.append(", comment=").append(comment);
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
        SuperProFunArg other = (SuperProFunArg) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFunId() == null ? other.getFunId() == null : this.getFunId().equals(other.getFunId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getArgType() == null ? other.getArgType() == null : this.getArgType().equals(other.getArgType()))
            && (this.getCollectionType() == null ? other.getCollectionType() == null : this.getCollectionType().equals(other.getCollectionType()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFunId() == null) ? 0 : getFunId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getArgType() == null) ? 0 : getArgType().hashCode());
        result = prime * result + ((getCollectionType() == null) ? 0 : getCollectionType().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}