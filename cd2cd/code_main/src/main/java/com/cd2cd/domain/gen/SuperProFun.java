package com.cd2cd.domain.gen;

import java.io.Serializable;
import java.util.Date;

public class SuperProFun implements Serializable {
    private Long id;

    private Long cid;

    private String name;

    private String comment;

    /**
     * 方法名称
     */
    private String funName;

    /**
     * request method: post/get/del/put
     */
    private String reqMethod;

    /**
     * request mapping
     */
    private String reqPath;

    /**
     * return type: string / vo / page
     */
    private String resType;

    /**
     * return vo
     */
    private Long resVoId;

    private Long resPageId;

    private String returnVo;

    /**
     * 在方法中显示使用
     */
    private String returnShow;

    /**
     * todo
     */
    private String todoContent;

    private String genService;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
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
     * 方法名称
     */
    public String getFunName() {
        return funName;
    }

    /**
     * 方法名称
     */
    public void setFunName(String funName) {
        this.funName = funName;
    }

    /**
     * request method: post/get/del/put
     */
    public String getReqMethod() {
        return reqMethod;
    }

    /**
     * request method: post/get/del/put
     */
    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    /**
     * request mapping
     */
    public String getReqPath() {
        return reqPath;
    }

    /**
     * request mapping
     */
    public void setReqPath(String reqPath) {
        this.reqPath = reqPath;
    }

    /**
     * return type: string / vo / page
     */
    public String getResType() {
        return resType;
    }

    /**
     * return type: string / vo / page
     */
    public void setResType(String resType) {
        this.resType = resType;
    }

    /**
     * return vo
     */
    public Long getResVoId() {
        return resVoId;
    }

    /**
     * return vo
     */
    public void setResVoId(Long resVoId) {
        this.resVoId = resVoId;
    }

    public Long getResPageId() {
        return resPageId;
    }

    public void setResPageId(Long resPageId) {
        this.resPageId = resPageId;
    }

    public String getReturnVo() {
        return returnVo;
    }

    public void setReturnVo(String returnVo) {
        this.returnVo = returnVo;
    }

    /**
     * 在方法中显示使用
     */
    public String getReturnShow() {
        return returnShow;
    }

    /**
     * 在方法中显示使用
     */
    public void setReturnShow(String returnShow) {
        this.returnShow = returnShow;
    }

    /**
     * todo
     */
    public String getTodoContent() {
        return todoContent;
    }

    /**
     * todo
     */
    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public String getGenService() {
        return genService;
    }

    public void setGenService(String genService) {
        this.genService = genService;
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
        sb.append(", cid=").append(cid);
        sb.append(", name=").append(name);
        sb.append(", comment=").append(comment);
        sb.append(", funName=").append(funName);
        sb.append(", reqMethod=").append(reqMethod);
        sb.append(", reqPath=").append(reqPath);
        sb.append(", resType=").append(resType);
        sb.append(", resVoId=").append(resVoId);
        sb.append(", resPageId=").append(resPageId);
        sb.append(", returnVo=").append(returnVo);
        sb.append(", returnShow=").append(returnShow);
        sb.append(", todoContent=").append(todoContent);
        sb.append(", genService=").append(genService);
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
        SuperProFun other = (SuperProFun) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getFunName() == null ? other.getFunName() == null : this.getFunName().equals(other.getFunName()))
            && (this.getReqMethod() == null ? other.getReqMethod() == null : this.getReqMethod().equals(other.getReqMethod()))
            && (this.getReqPath() == null ? other.getReqPath() == null : this.getReqPath().equals(other.getReqPath()))
            && (this.getResType() == null ? other.getResType() == null : this.getResType().equals(other.getResType()))
            && (this.getResVoId() == null ? other.getResVoId() == null : this.getResVoId().equals(other.getResVoId()))
            && (this.getResPageId() == null ? other.getResPageId() == null : this.getResPageId().equals(other.getResPageId()))
            && (this.getReturnVo() == null ? other.getReturnVo() == null : this.getReturnVo().equals(other.getReturnVo()))
            && (this.getReturnShow() == null ? other.getReturnShow() == null : this.getReturnShow().equals(other.getReturnShow()))
            && (this.getTodoContent() == null ? other.getTodoContent() == null : this.getTodoContent().equals(other.getTodoContent()))
            && (this.getGenService() == null ? other.getGenService() == null : this.getGenService().equals(other.getGenService()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getFunName() == null) ? 0 : getFunName().hashCode());
        result = prime * result + ((getReqMethod() == null) ? 0 : getReqMethod().hashCode());
        result = prime * result + ((getReqPath() == null) ? 0 : getReqPath().hashCode());
        result = prime * result + ((getResType() == null) ? 0 : getResType().hashCode());
        result = prime * result + ((getResVoId() == null) ? 0 : getResVoId().hashCode());
        result = prime * result + ((getResPageId() == null) ? 0 : getResPageId().hashCode());
        result = prime * result + ((getReturnVo() == null) ? 0 : getReturnVo().hashCode());
        result = prime * result + ((getReturnShow() == null) ? 0 : getReturnShow().hashCode());
        result = prime * result + ((getTodoContent() == null) ? 0 : getTodoContent().hashCode());
        result = prime * result + ((getGenService() == null) ? 0 : getGenService().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}