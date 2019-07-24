

CREATE TABLE IF NOT EXISTS `comm_validate` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '验证名：max,min,notnull',
  `comment` varchar(200) DEFAULT NULL COMMENT '验证说明： 不可为空',
  `args` varchar(300) DEFAULT NULL COMMENT '验证参数：["type:int","name:String"]',
  `pro_id` bigint(11) DEFAULT NULL COMMENT '项目ID； 值为0时为对所有项目公开使用',
  PRIMARY KEY (`id`)
) COMMENT='验证';


CREATE TABLE IF NOT EXISTS `pro_database` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `db_name` varchar(30) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `port` varchar(12) DEFAULT NULL,
  `hostname` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `pro_field` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `file_id` bigint(11) DEFAULT NULL COMMENT '属于文件ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称：英文显示，如：username',
  `data_type` varchar(30) DEFAULT NULL COMMENT '类类型:基本数据类型：base，自定义对象：vo，范型：T',
  `type_path` varchar(100) DEFAULT NULL COMMENT '类路径，如:String、com.user.UserVo',
  `type_key` varchar(100) DEFAULT NULL COMMENT '当data_type为vo时值为vo_id,',
  `collection_type` varchar(50) DEFAULT NULL COMMENT '集合类型:单值：single，列表：list，集合：set，Map：map',
  `comment` varchar(150) DEFAULT NULL COMMENT '字段备注',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `pro_file` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) unsigned DEFAULT NULL COMMENT '项目ID',
  `module_id` bigint(11) unsigned DEFAULT NULL COMMENT '模块ID',
  `super_id` bigint(11) unsigned DEFAULT NULL COMMENT '父类ID',
  `name` varchar(100) DEFAULT NULL COMMENT '文件名称',
  `comment` varchar(500) DEFAULT NULL,
  `req_path` varchar(100) DEFAULT NULL COMMENT 'file_type: controller，时有效',
  `file_type` varchar(20) DEFAULT NULL COMMENT '文件类型：controller|service|vo|dao|domain|',
  `paradigm` varchar(5) NOT NULL DEFAULT 'no' COMMENT '是否为范型:no\\yes',
  `class_type` varchar(10) NOT NULL DEFAULT 'class' COMMENT '类类型：class|generics|enum|interface|abstruct',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_id` (`project_id`,`name`,`class_type`,`module_id`),
);

CREATE TABLE IF NOT EXISTS `pro_fun` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `cid` bigint(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `fun_name` varchar(100) DEFAULT NULL COMMENT '方法名称',
  `req_method` varchar(15) DEFAULT NULL COMMENT 'request method: post/get/del/put',
  `req_path` varchar(100) DEFAULT NULL COMMENT 'request mapping',
  `res_type` varchar(10) DEFAULT NULL COMMENT 'return type: string / vo / page',
  `res_vo_id` bigint(11) DEFAULT NULL COMMENT 'return vo',
  `res_page_id` bigint(11) DEFAULT NULL,
  `return_vo` varchar(150) DEFAULT NULL,
  `return_show` varchar(100) DEFAULT NULL COMMENT '在方法中显示使用',
  `todo_content` varchar(500) DEFAULT NULL COMMENT 'todo',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `pro_fun_arg` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `fun_id` bigint(20) DEFAULT NULL COMMENT '方法ID',
  `pid` bigint(11) NOT NULL DEFAULT '0' COMMENT '上一级ID用于VO',
  `name` varchar(50) DEFAULT NULL COMMENT '参数名称，英文',
  `arg_type` varchar(50) DEFAULT '' COMMENT '参数类型: base、vo 、pojo',
  `arg_type_name` varchar(100) DEFAULT NULL COMMENT '类型名称：String,Integer,User',
  `arg_type_id` bigint(11) DEFAULT NULL COMMENT '类型ID：当arg_type为vo时:vo_id;pojo:id',
  `collection_type` varchar(50) DEFAULT 'single' COMMENT '集合:single/list/map/set',
  `valid` varchar(500) DEFAULT NULL COMMENT '字段验证JSON数组',
  `comment` varchar(150) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fun_id` (`fun_id`,`name`)
);

CREATE TABLE IF NOT EXISTS `pro_fun_retval` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL COMMENT 'page/vo/basetype/void',
  `target_id` int(11) unsigned DEFAULT NULL COMMENT 'page_id/vo_id',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `pro_module` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) unsigned NOT NULL COMMENT '项目ID',
  `name` varchar(80) DEFAULT NULL COMMENT '程序名称',
  `show_name` varchar(80) DEFAULT NULL COMMENT '显示名称',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
);

CREATE TABLE IF NOT EXISTS `pro_page` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) DEFAULT NULL,
  `module_id` bigint(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `pro_page_act` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);

CREATE TABLE `pro_project` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '项目名称',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '项目介绍',
  `group_id` varchar(50) NOT NULL DEFAULT '' COMMENT '项目组织',
  `artifact_id` varchar(50) NOT NULL DEFAULT '' COMMENT '项目名',
  `package_type` varchar(10) NOT NULL DEFAULT '' COMMENT '包结构类型：standard、module',
  `version` varchar(30) NOT NULL DEFAULT '' COMMENT '项目版本',
  `context_path` varchar(50) NOT NULL DEFAULT '' COMMENT '访问路径',
  `local_path` varchar(300) DEFAULT NULL COMMENT '本地路径，用于本地开发使用',
  `ignore_tables` varchar(500) DEFAULT NULL COMMENT '不生成vo',
  `git_url` varchar(150) DEFAULT NULL,
  `git_account` varchar(30) DEFAULT NULL,
  `git_password` varchar(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
);



CREATE TABLE IF NOT EXISTS `pro_project_database_rel` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) unsigned NOT NULL COMMENT '项目ID',
  `database_id` bigint(11) unsigned NOT NULL COMMENT '数据库ID',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
);


CREATE TABLE IF NOT EXISTS `pro_table` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `database_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `name` varchar(50) DEFAULT NULL,
  `comment` varchar(150) DEFAULT NULL,
  `emgome_type` varchar(10) DEFAULT NULL COMMENT '数据库引擎类型：InnoDB/MyISAM/',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
);

CREATE TABLE IF NOT EXISTS `pro_table_column` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `table_id` bigint(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '字段名称',
  `comment` varchar(100) DEFAULT NULL COMMENT '注释',
  `mysql_type` varchar(50) DEFAULT NULL COMMENT '数据类型',
  `default_value` varchar(100) DEFAULT NULL COMMENT '默认值',
  `allow_null` varchar(11) DEFAULT NULL COMMENT '是否充许为空',
  `key_type` varchar(15) DEFAULT NULL COMMENT '主键:PRI，搜索:MUL，唯一:UNI',
  `extra` varchar(30) DEFAULT '' COMMENT '额外信息:AUTO_INCREMENT',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
);

CREATE TABLE IF NOT EXISTS `sys_authority` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` varchar(50) DEFAULT NULL COMMENT '上级id',
  `guid` varchar(50) DEFAULT NULL COMMENT '权限ID号',
  `name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `url` varchar(200) DEFAULT NULL COMMENT '访问url',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
);

CREATE TABLE IF NOT EXISTS `sys_authority_role_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `authorities_id` varchar(50) NOT NULL,
  `role_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
);


CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '角色名',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL DEFAULT '',
  `nickname` varchar(20) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `email` varchar(36) DEFAULT NULL,
  `password` varchar(32) NOT NULL DEFAULT '',
  `status` varchar(20) NOT NULL DEFAULT 'enable' COMMENT 'enable,disable',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
);
CREATE TABLE IF NOT EXISTS `sys_user_role_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `role_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
);