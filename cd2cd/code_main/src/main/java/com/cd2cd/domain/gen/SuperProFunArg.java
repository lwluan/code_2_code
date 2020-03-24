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
     * 上一级ID用于VO
     */
    private Long pid;

    /**
     * 参数名称，英文
     */
    private String name;

    /**
     * 参数类型: base、vo 、pojo
     */
    private String argType;

    /**
     * 类型名称：String,Integer,User
     */
    private String argTypeName;

    /**
     * 类型ID：当arg_type为vo时:vo_id;pojo:id
     */
    private Long argTypeId;

    /**
     * 集合:single/list/map/set
     */
    private String collectionType;

    /**
     * 字段验证JSON数组
     */
    private String valid;

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
     * 上一级ID用于VO
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 上一级ID用于VO
     */
    public void setPid(Long pid) {
        this.pid = pid;
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
     * 参数类型: base、vo 、pojo
     */
    public String getArgType() {
        return argType;
    }

    /**
     * 参数类型: base、vo 、pojo
     */
    public void setArgType(String argType) {
        this.argType = argType;
    }

    /**
     * 类型名称：String,Integer,User
     */
    public String getArgTypeName() {
        return argTypeName;
    }

    /**
     * 类型名称：String,Integer,User
     */
    public void setArgTypeName(String argTypeName) {
        this.argTypeName = argTypeName;
    }

    /**
     * 类型ID：当arg_type为vo时:vo_id;pojo:id
     */
    public Long getArgTypeId() {
        return argTypeId;
    }

    /**
     * 类型ID：当arg_type为vo时:vo_id;pojo:id
     */
    public void setArgTypeId(Long argTypeId) {
        this.argTypeId = argTypeId;
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
     * 字段验证JSON数组
     */
    public String getValid() {
        return valid;
    }

    /**
     * 字段验证JSON数组
     */
    public void setValid(String valid) {
        this.valid = valid;
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
        sb.append(", pid=").append(pid);
        sb.append(", name=").append(name);
        sb.append(", argType=").append(argType);
        sb.append(", argTypeName=").append(argTypeName);
        sb.append(", argTypeId=").append(argTypeId);
        sb.append(", collectionType=").append(collectionType);
        sb.append(", valid=").append(valid);
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
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getArgType() == null ? other.getArgType() == null : this.getArgType().equals(other.getArgType()))
            && (this.getArgTypeName() == null ? other.getArgTypeName() == null : this.getArgTypeName().equals(other.getArgTypeName()))
            && (this.getArgTypeId() == null ? other.getArgTypeId() == null : this.getArgTypeId().equals(other.getArgTypeId()))
            && (this.getCollectionType() == null ? other.getCollectionType() == null : this.getCollectionType().equals(other.getCollectionType()))
            && (this.getValid() == null ? other.getValid() == null : this.getValid().equals(other.getValid()))
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
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getArgType() == null) ? 0 : getArgType().hashCode());
        result = prime * result + ((getArgTypeName() == null) ? 0 : getArgTypeName().hashCode());
        result = prime * result + ((getArgTypeId() == null) ? 0 : getArgTypeId().hashCode());
        result = prime * result + ((getCollectionType() == null) ? 0 : getCollectionType().hashCode());
        result = prime * result + ((getValid() == null) ? 0 : getValid().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}