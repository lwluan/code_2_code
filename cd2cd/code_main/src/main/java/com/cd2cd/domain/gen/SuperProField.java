package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProField implements Serializable {
    private Long id;

    /**
     * 属于文件ID
     */
    private Long fileId;

    /**
     * 名称：英文显示，如：username
     */
    private String name;

    /**
     * 类类型:基本数据类型：base，自定义对象：vo，范型：T
     */
    private String dataType;

    /**
     * 类路径，如:String、com.user.UserVo
     */
    private String typePath;

    /**
     * 当data_type为vo时值为vo_id,
     */
    private String typeKey;

    /**
     * 集合类型:单值：single，列表：list，集合：set，Map：map
     */
    private String collectionType;

    /**
     * 字段备注
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
     * 属于文件ID
     */
    public Long getFileId() {
        return fileId;
    }

    /**
     * 属于文件ID
     */
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    /**
     * 名称：英文显示，如：username
     */
    public String getName() {
        return name;
    }

    /**
     * 名称：英文显示，如：username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 类类型:基本数据类型：base，自定义对象：vo，范型：T
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 类类型:基本数据类型：base，自定义对象：vo，范型：T
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 类路径，如:String、com.user.UserVo
     */
    public String getTypePath() {
        return typePath;
    }

    /**
     * 类路径，如:String、com.user.UserVo
     */
    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }

    /**
     * 当data_type为vo时值为vo_id,
     */
    public String getTypeKey() {
        return typeKey;
    }

    /**
     * 当data_type为vo时值为vo_id,
     */
    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    /**
     * 集合类型:单值：single，列表：list，集合：set，Map：map
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * 集合类型:单值：single，列表：list，集合：set，Map：map
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * 字段备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 字段备注
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
        sb.append(", fileId=").append(fileId);
        sb.append(", name=").append(name);
        sb.append(", dataType=").append(dataType);
        sb.append(", typePath=").append(typePath);
        sb.append(", typeKey=").append(typeKey);
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
        SuperProField other = (SuperProField) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
            && (this.getTypePath() == null ? other.getTypePath() == null : this.getTypePath().equals(other.getTypePath()))
            && (this.getTypeKey() == null ? other.getTypeKey() == null : this.getTypeKey().equals(other.getTypeKey()))
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
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getTypePath() == null) ? 0 : getTypePath().hashCode());
        result = prime * result + ((getTypeKey() == null) ? 0 : getTypeKey().hashCode());
        result = prime * result + ((getCollectionType() == null) ? 0 : getCollectionType().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}