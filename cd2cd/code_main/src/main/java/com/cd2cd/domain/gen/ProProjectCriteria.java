package com.cd2cd.domain.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProProjectCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer mysqlOffset;

    protected Integer mysqlLength;

    public ProProjectCriteria() {
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

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupIdEqualTo(String value) {
            addCriterion("group_id =", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotEqualTo(String value) {
            addCriterion("group_id <>", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThan(String value) {
            addCriterion("group_id >", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("group_id >=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThan(String value) {
            addCriterion("group_id <", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThanOrEqualTo(String value) {
            addCriterion("group_id <=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLike(String value) {
            addCriterion("group_id like", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotLike(String value) {
            addCriterion("group_id not like", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdIn(List<String> values) {
            addCriterion("group_id in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotIn(List<String> values) {
            addCriterion("group_id not in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdBetween(String value1, String value2) {
            addCriterion("group_id between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotBetween(String value1, String value2) {
            addCriterion("group_id not between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdIsNull() {
            addCriterion("artifact_id is null");
            return (Criteria) this;
        }

        public Criteria andArtifactIdIsNotNull() {
            addCriterion("artifact_id is not null");
            return (Criteria) this;
        }

        public Criteria andArtifactIdEqualTo(String value) {
            addCriterion("artifact_id =", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdNotEqualTo(String value) {
            addCriterion("artifact_id <>", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdGreaterThan(String value) {
            addCriterion("artifact_id >", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdGreaterThanOrEqualTo(String value) {
            addCriterion("artifact_id >=", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdLessThan(String value) {
            addCriterion("artifact_id <", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdLessThanOrEqualTo(String value) {
            addCriterion("artifact_id <=", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdLike(String value) {
            addCriterion("artifact_id like", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdNotLike(String value) {
            addCriterion("artifact_id not like", value, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdIn(List<String> values) {
            addCriterion("artifact_id in", values, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdNotIn(List<String> values) {
            addCriterion("artifact_id not in", values, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdBetween(String value1, String value2) {
            addCriterion("artifact_id between", value1, value2, "artifactId");
            return (Criteria) this;
        }

        public Criteria andArtifactIdNotBetween(String value1, String value2) {
            addCriterion("artifact_id not between", value1, value2, "artifactId");
            return (Criteria) this;
        }

        public Criteria andPackageTypeIsNull() {
            addCriterion("package_type is null");
            return (Criteria) this;
        }

        public Criteria andPackageTypeIsNotNull() {
            addCriterion("package_type is not null");
            return (Criteria) this;
        }

        public Criteria andPackageTypeEqualTo(String value) {
            addCriterion("package_type =", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeNotEqualTo(String value) {
            addCriterion("package_type <>", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeGreaterThan(String value) {
            addCriterion("package_type >", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeGreaterThanOrEqualTo(String value) {
            addCriterion("package_type >=", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeLessThan(String value) {
            addCriterion("package_type <", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeLessThanOrEqualTo(String value) {
            addCriterion("package_type <=", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeLike(String value) {
            addCriterion("package_type like", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeNotLike(String value) {
            addCriterion("package_type not like", value, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeIn(List<String> values) {
            addCriterion("package_type in", values, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeNotIn(List<String> values) {
            addCriterion("package_type not in", values, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeBetween(String value1, String value2) {
            addCriterion("package_type between", value1, value2, "packageType");
            return (Criteria) this;
        }

        public Criteria andPackageTypeNotBetween(String value1, String value2) {
            addCriterion("package_type not between", value1, value2, "packageType");
            return (Criteria) this;
        }

        public Criteria andProTypeIsNull() {
            addCriterion("pro_type is null");
            return (Criteria) this;
        }

        public Criteria andProTypeIsNotNull() {
            addCriterion("pro_type is not null");
            return (Criteria) this;
        }

        public Criteria andProTypeEqualTo(String value) {
            addCriterion("pro_type =", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeNotEqualTo(String value) {
            addCriterion("pro_type <>", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeGreaterThan(String value) {
            addCriterion("pro_type >", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeGreaterThanOrEqualTo(String value) {
            addCriterion("pro_type >=", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeLessThan(String value) {
            addCriterion("pro_type <", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeLessThanOrEqualTo(String value) {
            addCriterion("pro_type <=", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeLike(String value) {
            addCriterion("pro_type like", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeNotLike(String value) {
            addCriterion("pro_type not like", value, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeIn(List<String> values) {
            addCriterion("pro_type in", values, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeNotIn(List<String> values) {
            addCriterion("pro_type not in", values, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeBetween(String value1, String value2) {
            addCriterion("pro_type between", value1, value2, "proType");
            return (Criteria) this;
        }

        public Criteria andProTypeNotBetween(String value1, String value2) {
            addCriterion("pro_type not between", value1, value2, "proType");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("version like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("version not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andContextPathIsNull() {
            addCriterion("context_path is null");
            return (Criteria) this;
        }

        public Criteria andContextPathIsNotNull() {
            addCriterion("context_path is not null");
            return (Criteria) this;
        }

        public Criteria andContextPathEqualTo(String value) {
            addCriterion("context_path =", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathNotEqualTo(String value) {
            addCriterion("context_path <>", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathGreaterThan(String value) {
            addCriterion("context_path >", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathGreaterThanOrEqualTo(String value) {
            addCriterion("context_path >=", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathLessThan(String value) {
            addCriterion("context_path <", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathLessThanOrEqualTo(String value) {
            addCriterion("context_path <=", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathLike(String value) {
            addCriterion("context_path like", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathNotLike(String value) {
            addCriterion("context_path not like", value, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathIn(List<String> values) {
            addCriterion("context_path in", values, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathNotIn(List<String> values) {
            addCriterion("context_path not in", values, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathBetween(String value1, String value2) {
            addCriterion("context_path between", value1, value2, "contextPath");
            return (Criteria) this;
        }

        public Criteria andContextPathNotBetween(String value1, String value2) {
            addCriterion("context_path not between", value1, value2, "contextPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathIsNull() {
            addCriterion("local_path is null");
            return (Criteria) this;
        }

        public Criteria andLocalPathIsNotNull() {
            addCriterion("local_path is not null");
            return (Criteria) this;
        }

        public Criteria andLocalPathEqualTo(String value) {
            addCriterion("local_path =", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathNotEqualTo(String value) {
            addCriterion("local_path <>", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathGreaterThan(String value) {
            addCriterion("local_path >", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathGreaterThanOrEqualTo(String value) {
            addCriterion("local_path >=", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathLessThan(String value) {
            addCriterion("local_path <", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathLessThanOrEqualTo(String value) {
            addCriterion("local_path <=", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathLike(String value) {
            addCriterion("local_path like", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathNotLike(String value) {
            addCriterion("local_path not like", value, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathIn(List<String> values) {
            addCriterion("local_path in", values, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathNotIn(List<String> values) {
            addCriterion("local_path not in", values, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathBetween(String value1, String value2) {
            addCriterion("local_path between", value1, value2, "localPath");
            return (Criteria) this;
        }

        public Criteria andLocalPathNotBetween(String value1, String value2) {
            addCriterion("local_path not between", value1, value2, "localPath");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesIsNull() {
            addCriterion("ignore_tables is null");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesIsNotNull() {
            addCriterion("ignore_tables is not null");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesEqualTo(String value) {
            addCriterion("ignore_tables =", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesNotEqualTo(String value) {
            addCriterion("ignore_tables <>", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesGreaterThan(String value) {
            addCriterion("ignore_tables >", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesGreaterThanOrEqualTo(String value) {
            addCriterion("ignore_tables >=", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesLessThan(String value) {
            addCriterion("ignore_tables <", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesLessThanOrEqualTo(String value) {
            addCriterion("ignore_tables <=", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesLike(String value) {
            addCriterion("ignore_tables like", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesNotLike(String value) {
            addCriterion("ignore_tables not like", value, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesIn(List<String> values) {
            addCriterion("ignore_tables in", values, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesNotIn(List<String> values) {
            addCriterion("ignore_tables not in", values, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesBetween(String value1, String value2) {
            addCriterion("ignore_tables between", value1, value2, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andIgnoreTablesNotBetween(String value1, String value2) {
            addCriterion("ignore_tables not between", value1, value2, "ignoreTables");
            return (Criteria) this;
        }

        public Criteria andGitUrlIsNull() {
            addCriterion("git_url is null");
            return (Criteria) this;
        }

        public Criteria andGitUrlIsNotNull() {
            addCriterion("git_url is not null");
            return (Criteria) this;
        }

        public Criteria andGitUrlEqualTo(String value) {
            addCriterion("git_url =", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlNotEqualTo(String value) {
            addCriterion("git_url <>", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlGreaterThan(String value) {
            addCriterion("git_url >", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlGreaterThanOrEqualTo(String value) {
            addCriterion("git_url >=", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlLessThan(String value) {
            addCriterion("git_url <", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlLessThanOrEqualTo(String value) {
            addCriterion("git_url <=", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlLike(String value) {
            addCriterion("git_url like", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlNotLike(String value) {
            addCriterion("git_url not like", value, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlIn(List<String> values) {
            addCriterion("git_url in", values, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlNotIn(List<String> values) {
            addCriterion("git_url not in", values, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlBetween(String value1, String value2) {
            addCriterion("git_url between", value1, value2, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitUrlNotBetween(String value1, String value2) {
            addCriterion("git_url not between", value1, value2, "gitUrl");
            return (Criteria) this;
        }

        public Criteria andGitAccountIsNull() {
            addCriterion("git_account is null");
            return (Criteria) this;
        }

        public Criteria andGitAccountIsNotNull() {
            addCriterion("git_account is not null");
            return (Criteria) this;
        }

        public Criteria andGitAccountEqualTo(String value) {
            addCriterion("git_account =", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountNotEqualTo(String value) {
            addCriterion("git_account <>", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountGreaterThan(String value) {
            addCriterion("git_account >", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountGreaterThanOrEqualTo(String value) {
            addCriterion("git_account >=", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountLessThan(String value) {
            addCriterion("git_account <", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountLessThanOrEqualTo(String value) {
            addCriterion("git_account <=", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountLike(String value) {
            addCriterion("git_account like", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountNotLike(String value) {
            addCriterion("git_account not like", value, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountIn(List<String> values) {
            addCriterion("git_account in", values, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountNotIn(List<String> values) {
            addCriterion("git_account not in", values, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountBetween(String value1, String value2) {
            addCriterion("git_account between", value1, value2, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitAccountNotBetween(String value1, String value2) {
            addCriterion("git_account not between", value1, value2, "gitAccount");
            return (Criteria) this;
        }

        public Criteria andGitPasswordIsNull() {
            addCriterion("git_password is null");
            return (Criteria) this;
        }

        public Criteria andGitPasswordIsNotNull() {
            addCriterion("git_password is not null");
            return (Criteria) this;
        }

        public Criteria andGitPasswordEqualTo(String value) {
            addCriterion("git_password =", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordNotEqualTo(String value) {
            addCriterion("git_password <>", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordGreaterThan(String value) {
            addCriterion("git_password >", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("git_password >=", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordLessThan(String value) {
            addCriterion("git_password <", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordLessThanOrEqualTo(String value) {
            addCriterion("git_password <=", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordLike(String value) {
            addCriterion("git_password like", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordNotLike(String value) {
            addCriterion("git_password not like", value, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordIn(List<String> values) {
            addCriterion("git_password in", values, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordNotIn(List<String> values) {
            addCriterion("git_password not in", values, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordBetween(String value1, String value2) {
            addCriterion("git_password between", value1, value2, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andGitPasswordNotBetween(String value1, String value2) {
            addCriterion("git_password not between", value1, value2, "gitPassword");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNull() {
            addCriterion("del_flag is null");
            return (Criteria) this;
        }

        public Criteria andDelFlagIsNotNull() {
            addCriterion("del_flag is not null");
            return (Criteria) this;
        }

        public Criteria andDelFlagEqualTo(Integer value) {
            addCriterion("del_flag =", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotEqualTo(Integer value) {
            addCriterion("del_flag <>", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThan(Integer value) {
            addCriterion("del_flag >", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagGreaterThanOrEqualTo(Integer value) {
            addCriterion("del_flag >=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThan(Integer value) {
            addCriterion("del_flag <", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagLessThanOrEqualTo(Integer value) {
            addCriterion("del_flag <=", value, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagIn(List<Integer> values) {
            addCriterion("del_flag in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotIn(List<Integer> values) {
            addCriterion("del_flag not in", values, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagBetween(Integer value1, Integer value2) {
            addCriterion("del_flag between", value1, value2, "delFlag");
            return (Criteria) this;
        }

        public Criteria andDelFlagNotBetween(Integer value1, Integer value2) {
            addCriterion("del_flag not between", value1, value2, "delFlag");
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