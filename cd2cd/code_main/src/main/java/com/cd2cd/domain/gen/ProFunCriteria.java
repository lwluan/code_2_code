package com.cd2cd.domain.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProFunCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer mysqlOffset;

    protected Integer mysqlLength;

    public ProFunCriteria() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * @param mysqlOffset 
	 *            指定返回记录行的偏移量<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public void setMysqlOffset(Integer mysqlOffset) {
        this.mysqlOffset=mysqlOffset;
    }

    /**
     * @param mysqlLength 
	 *            指定返回记录行的最大数目<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public Integer getMysqlOffset() {
        return mysqlOffset;
    }

    /**
     * @param mysqlOffset 
	 *            指定返回记录行的偏移量<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public void setMysqlLength(Integer mysqlLength) {
        this.mysqlLength=mysqlLength;
    }

    /**
     * @param mysqlLength 
	 *            指定返回记录行的最大数目<br>
	 *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15
     */
    public Integer getMysqlLength() {
        return mysqlLength;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCidIsNull() {
            addCriterion("cid is null");
            return (Criteria) this;
        }

        public Criteria andCidIsNotNull() {
            addCriterion("cid is not null");
            return (Criteria) this;
        }

        public Criteria andCidEqualTo(Long value) {
            addCriterion("cid =", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotEqualTo(Long value) {
            addCriterion("cid <>", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThan(Long value) {
            addCriterion("cid >", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidGreaterThanOrEqualTo(Long value) {
            addCriterion("cid >=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThan(Long value) {
            addCriterion("cid <", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidLessThanOrEqualTo(Long value) {
            addCriterion("cid <=", value, "cid");
            return (Criteria) this;
        }

        public Criteria andCidIn(List<Long> values) {
            addCriterion("cid in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotIn(List<Long> values) {
            addCriterion("cid not in", values, "cid");
            return (Criteria) this;
        }

        public Criteria andCidBetween(Long value1, Long value2) {
            addCriterion("cid between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andCidNotBetween(Long value1, Long value2) {
            addCriterion("cid not between", value1, value2, "cid");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("comment is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("comment is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(String value) {
            addCriterion("comment =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(String value) {
            addCriterion("comment <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(String value) {
            addCriterion("comment >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(String value) {
            addCriterion("comment >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(String value) {
            addCriterion("comment <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(String value) {
            addCriterion("comment <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLike(String value) {
            addCriterion("comment like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotLike(String value) {
            addCriterion("comment not like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<String> values) {
            addCriterion("comment in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<String> values) {
            addCriterion("comment not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(String value1, String value2) {
            addCriterion("comment between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(String value1, String value2) {
            addCriterion("comment not between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andFunNameIsNull() {
            addCriterion("fun_name is null");
            return (Criteria) this;
        }

        public Criteria andFunNameIsNotNull() {
            addCriterion("fun_name is not null");
            return (Criteria) this;
        }

        public Criteria andFunNameEqualTo(String value) {
            addCriterion("fun_name =", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameNotEqualTo(String value) {
            addCriterion("fun_name <>", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameGreaterThan(String value) {
            addCriterion("fun_name >", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameGreaterThanOrEqualTo(String value) {
            addCriterion("fun_name >=", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameLessThan(String value) {
            addCriterion("fun_name <", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameLessThanOrEqualTo(String value) {
            addCriterion("fun_name <=", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameLike(String value) {
            addCriterion("fun_name like", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameNotLike(String value) {
            addCriterion("fun_name not like", value, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameIn(List<String> values) {
            addCriterion("fun_name in", values, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameNotIn(List<String> values) {
            addCriterion("fun_name not in", values, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameBetween(String value1, String value2) {
            addCriterion("fun_name between", value1, value2, "funName");
            return (Criteria) this;
        }

        public Criteria andFunNameNotBetween(String value1, String value2) {
            addCriterion("fun_name not between", value1, value2, "funName");
            return (Criteria) this;
        }

        public Criteria andReqMethodIsNull() {
            addCriterion("req_method is null");
            return (Criteria) this;
        }

        public Criteria andReqMethodIsNotNull() {
            addCriterion("req_method is not null");
            return (Criteria) this;
        }

        public Criteria andReqMethodEqualTo(String value) {
            addCriterion("req_method =", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodNotEqualTo(String value) {
            addCriterion("req_method <>", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodGreaterThan(String value) {
            addCriterion("req_method >", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodGreaterThanOrEqualTo(String value) {
            addCriterion("req_method >=", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodLessThan(String value) {
            addCriterion("req_method <", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodLessThanOrEqualTo(String value) {
            addCriterion("req_method <=", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodLike(String value) {
            addCriterion("req_method like", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodNotLike(String value) {
            addCriterion("req_method not like", value, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodIn(List<String> values) {
            addCriterion("req_method in", values, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodNotIn(List<String> values) {
            addCriterion("req_method not in", values, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodBetween(String value1, String value2) {
            addCriterion("req_method between", value1, value2, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqMethodNotBetween(String value1, String value2) {
            addCriterion("req_method not between", value1, value2, "reqMethod");
            return (Criteria) this;
        }

        public Criteria andReqPathIsNull() {
            addCriterion("req_path is null");
            return (Criteria) this;
        }

        public Criteria andReqPathIsNotNull() {
            addCriterion("req_path is not null");
            return (Criteria) this;
        }

        public Criteria andReqPathEqualTo(String value) {
            addCriterion("req_path =", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathNotEqualTo(String value) {
            addCriterion("req_path <>", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathGreaterThan(String value) {
            addCriterion("req_path >", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathGreaterThanOrEqualTo(String value) {
            addCriterion("req_path >=", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathLessThan(String value) {
            addCriterion("req_path <", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathLessThanOrEqualTo(String value) {
            addCriterion("req_path <=", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathLike(String value) {
            addCriterion("req_path like", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathNotLike(String value) {
            addCriterion("req_path not like", value, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathIn(List<String> values) {
            addCriterion("req_path in", values, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathNotIn(List<String> values) {
            addCriterion("req_path not in", values, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathBetween(String value1, String value2) {
            addCriterion("req_path between", value1, value2, "reqPath");
            return (Criteria) this;
        }

        public Criteria andReqPathNotBetween(String value1, String value2) {
            addCriterion("req_path not between", value1, value2, "reqPath");
            return (Criteria) this;
        }

        public Criteria andResTypeIsNull() {
            addCriterion("res_type is null");
            return (Criteria) this;
        }

        public Criteria andResTypeIsNotNull() {
            addCriterion("res_type is not null");
            return (Criteria) this;
        }

        public Criteria andResTypeEqualTo(String value) {
            addCriterion("res_type =", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotEqualTo(String value) {
            addCriterion("res_type <>", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeGreaterThan(String value) {
            addCriterion("res_type >", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeGreaterThanOrEqualTo(String value) {
            addCriterion("res_type >=", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeLessThan(String value) {
            addCriterion("res_type <", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeLessThanOrEqualTo(String value) {
            addCriterion("res_type <=", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeLike(String value) {
            addCriterion("res_type like", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotLike(String value) {
            addCriterion("res_type not like", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeIn(List<String> values) {
            addCriterion("res_type in", values, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotIn(List<String> values) {
            addCriterion("res_type not in", values, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeBetween(String value1, String value2) {
            addCriterion("res_type between", value1, value2, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotBetween(String value1, String value2) {
            addCriterion("res_type not between", value1, value2, "resType");
            return (Criteria) this;
        }

        public Criteria andResVoIdIsNull() {
            addCriterion("res_vo_id is null");
            return (Criteria) this;
        }

        public Criteria andResVoIdIsNotNull() {
            addCriterion("res_vo_id is not null");
            return (Criteria) this;
        }

        public Criteria andResVoIdEqualTo(Long value) {
            addCriterion("res_vo_id =", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdNotEqualTo(Long value) {
            addCriterion("res_vo_id <>", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdGreaterThan(Long value) {
            addCriterion("res_vo_id >", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdGreaterThanOrEqualTo(Long value) {
            addCriterion("res_vo_id >=", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdLessThan(Long value) {
            addCriterion("res_vo_id <", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdLessThanOrEqualTo(Long value) {
            addCriterion("res_vo_id <=", value, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdIn(List<Long> values) {
            addCriterion("res_vo_id in", values, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdNotIn(List<Long> values) {
            addCriterion("res_vo_id not in", values, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdBetween(Long value1, Long value2) {
            addCriterion("res_vo_id between", value1, value2, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResVoIdNotBetween(Long value1, Long value2) {
            addCriterion("res_vo_id not between", value1, value2, "resVoId");
            return (Criteria) this;
        }

        public Criteria andResPageIdIsNull() {
            addCriterion("res_page_id is null");
            return (Criteria) this;
        }

        public Criteria andResPageIdIsNotNull() {
            addCriterion("res_page_id is not null");
            return (Criteria) this;
        }

        public Criteria andResPageIdEqualTo(Long value) {
            addCriterion("res_page_id =", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdNotEqualTo(Long value) {
            addCriterion("res_page_id <>", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdGreaterThan(Long value) {
            addCriterion("res_page_id >", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdGreaterThanOrEqualTo(Long value) {
            addCriterion("res_page_id >=", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdLessThan(Long value) {
            addCriterion("res_page_id <", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdLessThanOrEqualTo(Long value) {
            addCriterion("res_page_id <=", value, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdIn(List<Long> values) {
            addCriterion("res_page_id in", values, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdNotIn(List<Long> values) {
            addCriterion("res_page_id not in", values, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdBetween(Long value1, Long value2) {
            addCriterion("res_page_id between", value1, value2, "resPageId");
            return (Criteria) this;
        }

        public Criteria andResPageIdNotBetween(Long value1, Long value2) {
            addCriterion("res_page_id not between", value1, value2, "resPageId");
            return (Criteria) this;
        }

        public Criteria andReturnVoIsNull() {
            addCriterion("return_vo is null");
            return (Criteria) this;
        }

        public Criteria andReturnVoIsNotNull() {
            addCriterion("return_vo is not null");
            return (Criteria) this;
        }

        public Criteria andReturnVoEqualTo(String value) {
            addCriterion("return_vo =", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoNotEqualTo(String value) {
            addCriterion("return_vo <>", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoGreaterThan(String value) {
            addCriterion("return_vo >", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoGreaterThanOrEqualTo(String value) {
            addCriterion("return_vo >=", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoLessThan(String value) {
            addCriterion("return_vo <", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoLessThanOrEqualTo(String value) {
            addCriterion("return_vo <=", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoLike(String value) {
            addCriterion("return_vo like", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoNotLike(String value) {
            addCriterion("return_vo not like", value, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoIn(List<String> values) {
            addCriterion("return_vo in", values, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoNotIn(List<String> values) {
            addCriterion("return_vo not in", values, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoBetween(String value1, String value2) {
            addCriterion("return_vo between", value1, value2, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnVoNotBetween(String value1, String value2) {
            addCriterion("return_vo not between", value1, value2, "returnVo");
            return (Criteria) this;
        }

        public Criteria andReturnShowIsNull() {
            addCriterion("return_show is null");
            return (Criteria) this;
        }

        public Criteria andReturnShowIsNotNull() {
            addCriterion("return_show is not null");
            return (Criteria) this;
        }

        public Criteria andReturnShowEqualTo(String value) {
            addCriterion("return_show =", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowNotEqualTo(String value) {
            addCriterion("return_show <>", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowGreaterThan(String value) {
            addCriterion("return_show >", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowGreaterThanOrEqualTo(String value) {
            addCriterion("return_show >=", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowLessThan(String value) {
            addCriterion("return_show <", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowLessThanOrEqualTo(String value) {
            addCriterion("return_show <=", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowLike(String value) {
            addCriterion("return_show like", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowNotLike(String value) {
            addCriterion("return_show not like", value, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowIn(List<String> values) {
            addCriterion("return_show in", values, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowNotIn(List<String> values) {
            addCriterion("return_show not in", values, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowBetween(String value1, String value2) {
            addCriterion("return_show between", value1, value2, "returnShow");
            return (Criteria) this;
        }

        public Criteria andReturnShowNotBetween(String value1, String value2) {
            addCriterion("return_show not between", value1, value2, "returnShow");
            return (Criteria) this;
        }

        public Criteria andTodoContentIsNull() {
            addCriterion("todo_content is null");
            return (Criteria) this;
        }

        public Criteria andTodoContentIsNotNull() {
            addCriterion("todo_content is not null");
            return (Criteria) this;
        }

        public Criteria andTodoContentEqualTo(String value) {
            addCriterion("todo_content =", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentNotEqualTo(String value) {
            addCriterion("todo_content <>", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentGreaterThan(String value) {
            addCriterion("todo_content >", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentGreaterThanOrEqualTo(String value) {
            addCriterion("todo_content >=", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentLessThan(String value) {
            addCriterion("todo_content <", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentLessThanOrEqualTo(String value) {
            addCriterion("todo_content <=", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentLike(String value) {
            addCriterion("todo_content like", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentNotLike(String value) {
            addCriterion("todo_content not like", value, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentIn(List<String> values) {
            addCriterion("todo_content in", values, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentNotIn(List<String> values) {
            addCriterion("todo_content not in", values, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentBetween(String value1, String value2) {
            addCriterion("todo_content between", value1, value2, "todoContent");
            return (Criteria) this;
        }

        public Criteria andTodoContentNotBetween(String value1, String value2) {
            addCriterion("todo_content not between", value1, value2, "todoContent");
            return (Criteria) this;
        }

        public Criteria andGenServiceIsNull() {
            addCriterion("gen_service is null");
            return (Criteria) this;
        }

        public Criteria andGenServiceIsNotNull() {
            addCriterion("gen_service is not null");
            return (Criteria) this;
        }

        public Criteria andGenServiceEqualTo(String value) {
            addCriterion("gen_service =", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceNotEqualTo(String value) {
            addCriterion("gen_service <>", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceGreaterThan(String value) {
            addCriterion("gen_service >", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceGreaterThanOrEqualTo(String value) {
            addCriterion("gen_service >=", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceLessThan(String value) {
            addCriterion("gen_service <", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceLessThanOrEqualTo(String value) {
            addCriterion("gen_service <=", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceLike(String value) {
            addCriterion("gen_service like", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceNotLike(String value) {
            addCriterion("gen_service not like", value, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceIn(List<String> values) {
            addCriterion("gen_service in", values, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceNotIn(List<String> values) {
            addCriterion("gen_service not in", values, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceBetween(String value1, String value2) {
            addCriterion("gen_service between", value1, value2, "genService");
            return (Criteria) this;
        }

        public Criteria andGenServiceNotBetween(String value1, String value2) {
            addCriterion("gen_service not between", value1, value2, "genService");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}