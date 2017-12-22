package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProTableColumn implements Serializable {
    private Long id;

    private Long tableId;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 注释
     */
    private String comment;

    /**
     * 数据类型
     */
    private String mysqlType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否充许为空
     */
    private String allowNull;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    /**
     * 字段名称
     */
    public String getName() {
        return name;
    }

    /**
     * 字段名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 注释
     */
    public String getComment() {
        return comment;
    }

    /**
     * 注释
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 数据类型
     */
    public String getMysqlType() {
        return mysqlType;
    }

    /**
     * 数据类型
     */
    public void setMysqlType(String mysqlType) {
        this.mysqlType = mysqlType;
    }

    /**
     * 默认值
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 默认值
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * 是否充许为空
     */
    public String getAllowNull() {
        return allowNull;
    }

    /**
     * 是否充许为空
     */
    public void setAllowNull(String allowNull) {
        this.allowNull = allowNull;
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
        sb.append(", tableId=").append(tableId);
        sb.append(", name=").append(name);
        sb.append(", comment=").append(comment);
        sb.append(", mysqlType=").append(mysqlType);
        sb.append(", defaultValue=").append(defaultValue);
        sb.append(", allowNull=").append(allowNull);
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
        SuperProTableColumn other = (SuperProTableColumn) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTableId() == null ? other.getTableId() == null : this.getTableId().equals(other.getTableId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getMysqlType() == null ? other.getMysqlType() == null : this.getMysqlType().equals(other.getMysqlType()))
            && (this.getDefaultValue() == null ? other.getDefaultValue() == null : this.getDefaultValue().equals(other.getDefaultValue()))
            && (this.getAllowNull() == null ? other.getAllowNull() == null : this.getAllowNull().equals(other.getAllowNull()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTableId() == null) ? 0 : getTableId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getMysqlType() == null) ? 0 : getMysqlType().hashCode());
        result = prime * result + ((getDefaultValue() == null) ? 0 : getDefaultValue().hashCode());
        result = prime * result + ((getAllowNull() == null) ? 0 : getAllowNull().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}