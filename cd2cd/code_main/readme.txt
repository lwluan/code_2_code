1、生成项目 +++
2、下载项目
3、数据同步和保留
4、数据库添加表

5、单表快速配置： 生成controller\service\mapper， 可二次编辑
6、配置git 账号密码自动同步线上功能
7、生成程序文件或业务文件
8、生成基础表帮助


字体
http://www.bootcss.com/p/font-awesome/
- - - - - - - 
界面功能：
布局：工具操作栏：详情按钮、结构切换、模块管理、
1、项目详情；
2、结构切换：标准/模块
3、项目模块管理
4、controller\service\mapper\vo\domain 界面操作管理


读取数据表，显示左边树结构内容

- - - 
一个操作体组成部份
属于模块
文件注解
文件名称(类名)
依赖 文件
方法

- - - - -
树节点类型
# 文件包
	可添加操作
	
# 文件
	删除操作
	
# 页面
	可删除操作

- - - - - 
tabs 组件
tab: 标题，关闭按钮，鼠标划过效果
content: 显示组件内容

- - - - - - - - - - 
项目库管理：可依赖
db
redis
memcache
工作流
报表
登录认证
权限


- - - 项目 - db 关联 - - - -
详情、

- - - 数据库关系链建立
添加外键约束
只删除了关系，但对应的关联文件不会被删除。


- - - - 
静态组件和数据组件，组件可重复使用。

项目多数据库，数据源


show full columns from test;
show index from test;
show triggers from test;


// 项目中同步到数据操作中 数据库，数据库索引未同步操作。
# - - - - - 约束 - - - - - 
#是在表上强制执行地数据校验规则，主要用于保证数据库地完整性
/*
not null 
unique 唯一键tb_depttb_dept
primary key 
foreign key 外键
check 检查
*/


数据库同步过程中， ignore系统表格

生成VO对象改成基去项目设置不基于数据

### 自定义组件使用文档

### 去除数据库关联，需将，数据库表对应的Vo删除或置为无效
建议表添加字段 - 逻辑删除字段

### 前端请求数据统模块


### manager vo and edit data for return obj


#####
componet use demo
#####

2018-05-24
working vo edit
- super id +++
- toast show +++
- tab item show  +++
- vo property val is vo of T ***


- - - - - - 
no complete
- vo property select: when T type for property do

1、添加文件不刷新树  +
2、刷新树，不保存打开状态 +
3、extends select null+

controller of file type
1 save file and add req path +
2 delete file confirm : *** common del fun ++-
3 add fun for file empty ++
4 del fun for file empty and has data ++


1、update fun content +

# table pro_fun_retval 
id 

# controller fun return type 
1 select return type: page or vo +++
2 vo_id or page_id +++
3 vo_val_id +++
4 return data module
5 return type is T and set 
	- has one more T of type for property
	
	type:	base/vo/T
	
	collectionType: single/list/set/map
	
	BaseRes<CommRet<List<User>>> 
	[	{ type: 'vo', voName: 'CommRet', voId: 2, classType: 'T' }, 
		{ type: 'vo', voName: 'CommRet', voId: 2, classType: 'class' },
	];
	
test select page

###
params
# post json 
# request body
# get method : set value for vo one deep
# post json : one more deep set value
1、set parmas vo
2、set parmas data valid
 
down show for fun, 
column that pop set params
name / type / comment / valid(optional)  













