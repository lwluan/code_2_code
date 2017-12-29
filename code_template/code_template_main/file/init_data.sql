/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

INSERT INTO `sys_authority` (`pid`, `guid`, `name`, `url`, `create_time`, `update_time`)
VALUES
	(NULL, '101', '用户管理', '/sysUser/index', now(), now()),
	('101', '102', '查看用户', '/sysUser/detail', now(), now()),
	('101', '103', '修改用户', '/sysUser/modify', now(), now()),
	('101', '104', '删除用户', '/sysUser/del', now(), now()),
	('101', '105', '用户列表', '/sysUser/list', now(), now()),
	('101', '106', '添加用户', '/sysUser/add', now(), now()),
	(NULL, '201', '角色管理', '/sysRole/index', now(), now()),
	('201', '202', '修改角色', '/sysRole/modify', now(), now()),
	('201', '203', '查看角色', '/sysRole/detail', now(), now()),
	('201', '204', '删除角色', '/sysRole/del', now(), now()),
	('201', '205', '角色列表', '/sysRole/list', now(), now()),
	('201', '206', '添加角色', '/sysRole/add', now(), now()),
	(NULL, '301', '租户管理', '/zpTenant/index', now(), now()),
	('301', '302', '添加租户', '/zpTenant/add', now(), now()),
	('301', '303', '租户详情', '/zpTenant/detail', now(), now()),
	('301', '304', '修改租户', '/zpTenant/modify', now(), now()),
	('301', '305', '租户列表', '/zpTenant/list', now(), now()),
	('301', '306', '删除租户', '/zpTenant/del', now(), now());

--
-- 初始管理员用户数据
--
delete from `sys_user`;
INSERT INTO `sys_user` (`id`, `nickname`, `username`, `password`, `email`, `mobile`, `status`, `create_time`, `update_time`)
VALUES (1, '超级管理员', 'admin', '0e53d4d4d07a7b43ff40c91db2b1f4da', '', '', 'enable', now(), now());

-- 初始角色数据
delete from `sys_role`;
INSERT INTO `sys_role` (`id`, `name`, `description`, `create_time`, `update_time`)
VALUES (1, 'admin', '超级管理员', now(), now());
	
-- 用户和角色关联
delete from `sys_user_role_rel`;
INSERT INTO `sys_user_role_rel` (`user_id`, `role_id`)
VALUES (1, 1);

-- 权限和角色关联
INSERT INTO `sys_authority_role_rel` (`authorities_id`, `role_id`)
VALUES
	('101', 1), ('102', 1), ('103', 1), ('104', 1), ('105', 1), ('106', 1),
	('201', 1), ('202', 1), ('203', 1), ('204', 1), ('205', 1), ('206', 1),
	('301', 1), ('302', 1), ('303', 1), ('304', 1), ('305', 1), ('306', 1);

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
