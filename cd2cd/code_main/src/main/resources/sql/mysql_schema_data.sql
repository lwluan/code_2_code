# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.22)
# Database: auto_code
# Generation Time: 2019-04-16 11:57:50 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table comm_validate
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comm_validate`;

CREATE TABLE `comm_validate` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL COMMENT '验证名：max,min,notnull',
  `comment` varchar(200) DEFAULT NULL COMMENT '验证说明： 不可为空',
  `args` varchar(300) DEFAULT NULL COMMENT '验证参数：["type:int","name:String"]',
  `pro_id` bigint(11) DEFAULT NULL COMMENT '项目ID； 值为0时为对所有项目公开使用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='验证';



# Dump of table pro_database
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_database`;

CREATE TABLE `pro_database` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `db_name` varchar(30) DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `port` varchar(12) DEFAULT NULL,
  `hostname` varchar(30) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table pro_field
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_field`;

CREATE TABLE `pro_field` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件成员变量';



# Dump of table pro_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_file`;

CREATE TABLE `pro_file` (
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
  KEY `file_project_id_FK` (`project_id`),
  KEY `file_module_id_FK` (`module_id`),
  CONSTRAINT `file_module_id_FK` FOREIGN KEY (`module_id`) REFERENCES `pro_module` (`id`) ON DELETE CASCADE,
  CONSTRAINT `file_project_id_FK` FOREIGN KEY (`project_id`) REFERENCES `pro_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类';



# Dump of table pro_fun
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_fun`;

CREATE TABLE `pro_fun` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方法';



# Dump of table pro_fun_arg
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_fun_arg`;

CREATE TABLE `pro_fun_arg` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方法参数';



# Dump of table pro_fun_retval
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_fun_retval`;

CREATE TABLE `pro_fun_retval` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL COMMENT 'page/vo/basetype/void',
  `target_id` int(11) unsigned DEFAULT NULL COMMENT 'page_id/vo_id',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='方法返回值';



# Dump of table pro_module
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_module`;

CREATE TABLE `pro_module` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) unsigned NOT NULL COMMENT '项目ID',
  `name` varchar(80) DEFAULT NULL COMMENT '程序名称',
  `show_name` varchar(80) DEFAULT NULL COMMENT '显示名称',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `module_project_id_FK` (`project_id`),
  CONSTRAINT `module_project_id_FK` FOREIGN KEY (`project_id`) REFERENCES `pro_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块';



# Dump of table pro_page
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_page`;

CREATE TABLE `pro_page` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) DEFAULT NULL,
  `module_id` bigint(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面';



# Dump of table pro_page_act
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_page_act`;

CREATE TABLE `pro_page_act` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='页面调用方法';



# Dump of table pro_project
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_project`;

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
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目表';



# Dump of table pro_project_database_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_project_database_rel`;

CREATE TABLE `pro_project_database_rel` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` bigint(11) unsigned NOT NULL COMMENT '项目ID',
  `database_id` bigint(11) unsigned NOT NULL COMMENT '数据库ID',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id_FK` (`project_id`),
  KEY `database_id_FK` (`database_id`),
  CONSTRAINT `database_id_FK` FOREIGN KEY (`database_id`) REFERENCES `pro_database` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_id_FK` FOREIGN KEY (`project_id`) REFERENCES `pro_project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table pro_table
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_table`;

CREATE TABLE `pro_table` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `database_id` bigint(20) DEFAULT NULL COMMENT '项目ID',
  `name` varchar(50) DEFAULT NULL,
  `comment` varchar(150) DEFAULT NULL,
  `emgome_type` varchar(10) DEFAULT NULL COMMENT '数据库引擎类型：InnoDB/MyISAM/',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `database_id` (`database_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库表';



# Dump of table pro_table_column
# ------------------------------------------------------------

DROP TABLE IF EXISTS `pro_table_column`;

CREATE TABLE `pro_table_column` (
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
  UNIQUE KEY `table_id` (`table_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据库表字段';



# Dump of table sys_authority
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_authority`;

CREATE TABLE `sys_authority` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pid` varchar(50) DEFAULT NULL COMMENT '上级id',
  `guid` varchar(50) DEFAULT NULL COMMENT '权限ID号',
  `name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `url` varchar(200) DEFAULT NULL COMMENT '访问url',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `guid` (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限';



# Dump of table sys_authority_role_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_authority_role_rel`;

CREATE TABLE `sys_authority_role_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `authorities_id` varchar(50) NOT NULL,
  `role_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ar_unique` (`authorities_id`,`role_id`),
  KEY `role_id_FK` (`role_id`),
  CONSTRAINT `authorities_id_FK` FOREIGN KEY (`authorities_id`) REFERENCES `sys_authority` (`guid`) ON DELETE CASCADE,
  CONSTRAINT `role_id_FK` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限角色关联';



# Dump of table sys_role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '角色名',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色';



# Dump of table sys_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sys_user_role_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sys_user_role_rel`;

CREATE TABLE `sys_user_role_rel` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned DEFAULT NULL,
  `role_id` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ur_unique` (`user_id`,`role_id`),
  KEY `ur_role_id_FK` (`role_id`),
  CONSTRAINT `ur_role_id_FK` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ur_user_id_FK` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';


# Dump of table sys_authority
# ------------------------------------------------------------

LOCK TABLES `sys_authority` WRITE;
/*!40000 ALTER TABLE `sys_authority` DISABLE KEYS */;

INSERT INTO `sys_authority` (`id`, `pid`, `guid`, `name`, `url`, `create_time`, `update_time`)
VALUES
	(1,NULL,'101','用户管理','/sysUser/index','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(2,'101','102','查看用户','/sysUser/detail','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(3,'101','103','修改用户','/sysUser/modify','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(4,'101','104','删除用户','/sysUser/del','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(5,'101','105','用户列表','/sysUser/list','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(6,'101','106','添加用户','/sysUser/add','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(7,NULL,'201','角色管理','/sysRole/index','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(8,'201','202','修改角色','/sysRole/modify','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(9,'201','203','查看角色','/sysRole/detail','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(10,'201','204','删除角色','/sysRole/del','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(11,'201','205','角色列表','/sysRole/list','2017-12-18 22:42:00','2017-12-18 22:42:00'),
	(12,'201','206','添加角色','/sysRole/add','2017-12-18 22:42:00','2017-12-18 22:42:00');

/*!40000 ALTER TABLE `sys_authority` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_authority_role_rel
# ------------------------------------------------------------

LOCK TABLES `sys_authority_role_rel` WRITE;
/*!40000 ALTER TABLE `sys_authority_role_rel` DISABLE KEYS */;

INSERT INTO `sys_authority_role_rel` (`id`, `authorities_id`, `role_id`)
VALUES
	(1,'101',1),
	(2,'102',1),
	(3,'103',1),
	(4,'104',1),
	(5,'105',1),
	(6,'106',1),
	(7,'201',1),
	(8,'202',1),
	(9,'203',1),
	(10,'204',1),
	(11,'205',1),
	(12,'206',1);

/*!40000 ALTER TABLE `sys_authority_role_rel` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_role
# ------------------------------------------------------------

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;

INSERT INTO `sys_role` (`id`, `name`, `description`, `create_time`, `update_time`)
VALUES
	(1,'admin','超级管理员','2017-12-18 22:42:00','2017-12-18 22:42:00');

/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user
# ------------------------------------------------------------

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;

INSERT INTO `sys_user` (`id`, `username`, `nickname`, `mobile`, `email`, `password`, `status`, `create_time`, `update_time`)
VALUES
	(1,'admin','超级管理员','','','0e53d4d4d07a7b43ff40c91db2b1f4da','enable','2017-12-18 22:42:00','2017-12-18 22:42:00');

/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sys_user_role_rel
# ------------------------------------------------------------

LOCK TABLES `sys_user_role_rel` WRITE;
/*!40000 ALTER TABLE `sys_user_role_rel` DISABLE KEYS */;

INSERT INTO `sys_user_role_rel` (`id`, `user_id`, `role_id`)
VALUES
	(1,1,1);

/*!40000 ALTER TABLE `sys_user_role_rel` ENABLE KEYS */;
UNLOCK TABLES;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
