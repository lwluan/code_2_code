package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProTable implements Serializable {
    private Long id;

    /**
     * 项目ID
     */
    private Long databaseId;

    private String name;

    private String comment;

    /**
     * 数据库引擎类型：InnoDB/MyISAM/
     */
    private String emgomeType;

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
    public Long getDatabaseId() {
        return databaseId;
    }

    /**
     * 项目ID
     */
    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 数据库引擎类型：InnoDB/MyISAM/
     */
    public String getEmgomeType() {
        return emgomeType;
    }

    /**
     * 数据库引擎类型：InnoDB/MyISAM/
     */
    public void setEmgomeType(String emgomeType) {
        this.emgomeType = emgomeType;
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
        sb.append(", databaseId=").append(databaseId);
        sb.append(", name=").append(name);
        sb.append(", comment=").append(comment);
        sb.append(", emgomeType=").append(emgomeType);
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
        SuperProTable other = (SuperProTable) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDatabaseId() == null ? other.getDatabaseId() == null : this.getDatabaseId().equals(other.getDatabaseId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getEmgomeType() == null ? other.getEmgomeType() == null : this.getEmgomeType().equals(other.getEmgomeType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDatabaseId() == null) ? 0 : getDatabaseId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getEmgomeType() == null) ? 0 : getEmgomeType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}