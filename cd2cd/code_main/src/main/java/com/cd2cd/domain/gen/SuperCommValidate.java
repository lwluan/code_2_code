package com.cd2cd.domain.gen;

import java.io.Serializable;

public class SuperCommValidate implements Serializable {
    private Long id;

    /**
     * 验证名：max,min,notnull
     */
    private String name;

    /**
     * 验证说明： 不可为空
     */
    private String comment;

    /**
     * 验证参数：["type:int","name:String"]
     */
    private String args;

    /**
     * 项目ID； 值为0时为对所有项目公开使用
     */
    private Long proId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 验证名：max,min,notnull
     */
    public String getName() {
        return name;
    }

    /**
     * 验证名：max,min,notnull
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 验证说明： 不可为空
     */
    public String getComment() {
        return comment;
    }

    /**
     * 验证说明： 不可为空
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 验证参数：["type:int","name:String"]
     */
    public String getArgs() {
        return args;
    }

    /**
     * 验证参数：["type:int","name:String"]
     */
    public void setArgs(String args) {
        this.args = args;
    }

    /**
     * 项目ID； 值为0时为对所有项目公开使用
     */
    public Long getProId() {
        return proId;
    }

    /**
     * 项目ID； 值为0时为对所有项目公开使用
     */
    public void setProId(Long proId) {
        this.proId = proId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", comment=").append(comment);
        sb.append(", args=").append(args);
        sb.append(", proId=").append(proId);
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
        SuperCommValidate other = (SuperCommValidate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getArgs() == null ? other.getArgs() == null : this.getArgs().equals(other.getArgs()))
            && (this.getProId() == null ? other.getProId() == null : this.getProId().equals(other.getProId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getArgs() == null) ? 0 : getArgs().hashCode());
        result = prime * result + ((getProId() == null) ? 0 : getProId().hashCode());
        return result;
    }
}