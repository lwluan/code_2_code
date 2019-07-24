

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

INSERT INTO `sys_role` (`id`, `name`, `description`, `create_time`, `update_time`)
VALUES
	(1,'admin','超级管理员','2017-12-18 22:42:00','2017-12-18 22:42:00');

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
	
INSERT INTO `sys_user` (`id`, `username`, `nickname`, `mobile`, `email`, `password`, `status`, `create_time`, `update_time`)
VALUES
	(1,'admin','超级管理员','','','0e53d4d4d07a7b43ff40c91db2b1f4da','enable','2017-12-18 22:42:00','2017-12-18 22:42:00');

INSERT INTO `sys_user_role_rel` (`id`, `user_id`, `role_id`)
VALUES
	(1,1,1);
	

INSERT INTO `pro_file` (`id`, `project_id`, `module_id`, `super_id`, `name`, `comment`, `req_path`, `file_type`, `paradigm`, `class_type`, `create_time`, `update_time`)
VALUES
	(1, NULL, NULL, 0, 'BaseRes', '接口返回数据', NULL, 'vo', 'yes', 'class', '2018-05-17 15:21:08', '2018-05-17 15:21:08'),
	(2, NULL, NULL, 0, 'BaseReq', '用户请求数据', NULL, 'vo', 'yes', 'class', '2018-05-17 15:23:19', '2018-05-17 15:23:19');

INSERT INTO `pro_field` (`id`, `file_id`, `name`, `data_type`, `type_path`, `type_key`, `collection_type`, `comment`, `create_time`, `update_time`)
VALUES
	(1, 1, 'data', 'T', 'T', 'T', 'single', NULL, '2018-05-26 16:03:05', '2018-05-26 16:03:05'),
	(2, 1, 'msg', 'base', 'String', 'String', 'single', '消息', '2018-06-23 17:54:24', '2018-06-23 17:54:24'),
	(3, 1, 'code', 'base', 'Integer', 'Integer', 'single', NULL, '2018-06-23 17:54:31', '2018-06-23 17:54:31');

INSERT INTO `pro_field` (`id`, `file_id`, `name`, `data_type`, `type_path`, `type_key`, `collection_type`, `comment`, `create_time`, `update_time`)
VALUES
	(4, 2, 'currPage', 'base', 'Integer', 'Integer', 'single', NULL, '2018-06-23 17:53:13', '2018-06-23 17:53:13'),
	(5, 2, 'pageSize', 'base', 'Integer', 'Integer', 'single', NULL, '2018-06-23 17:53:22', '2018-06-23 17:53:22');
	
INSERT INTO `comm_validate` (`id`, `name`, `class_path`, `comment`, `args`, `pro_id`)
VALUES
	(1, 'NotNull', 'javax.validation.constraints.NotNull', '不可为空', NULL, NULL),
	(2, 'Mobile', 'javax.validation.constraints.Mobile', '手机', NULL, NULL),
	(3, 'Email', 'javax.validation.constraints.Email', '电子邮箱', NULL, NULL),
	(4, 'IdCard', 'javax.validation.constraints.IdCard', '身份证号', NULL, NULL),
	(5, 'Max', 'javax.validation.constraints.Max', '最大', '[\"value:Integer\"]', NULL),
	(6, 'Min', 'javax.validation.constraints.Min', '最小', '[\"value:Integer\"]', NULL),
	(7, 'Range', 'javax.validation.constraints.Range', '区间', '[\"max:Integer\", \"min:Integer\"]', NULL),
	(8, 'Valid', 'javax.validation.Valid', '是否验证', NULL, NULL);
	