# 运行使用
  先调用登录接口： /comm/admin/login   
  用户名和密码： admin 111111
  
  拿到token之后，所有请求带上token
  1、 header 带上: Authorization Bearer eyJ***
  2、 或 url 带上 token=fds*****
	
# 后台接口
	使用 /adapi/ 开头 如:   /adapi/sysUser
	不需要权限过滤使用  /comm/ 开头	
	
## 自动生成Mybatis 相关文件
	数据库表配文件：src/main/resources/pro_gen_tables.xml
	运行生成类：util/mbg/GenTest.java 
	插件相关类：util/mbg/*.java
	
		
功能：
1、列表字段控制
2、右则查询条件面板
	
	
	
build.sh 
start.sh
	
pom.xml name


	