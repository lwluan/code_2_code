<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>

	<context id="tables" targetRuntime="MyBatis3">
		<property name="beginningDelimiter" value="`"/>
		<property name="endingDelimiter" value="`"/>  
		<property name="mergeable" value="true"/>
		
		<!-- 插件是顺序执行 -->
		<plugin type="org.mybatis.generator.plugins.RenameExampleClassPlugin">
			<property name="searchString" value="Example$" />
			<property name="replaceString" value="Criteria" />
		</plugin>
		<plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
		<plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
		<plugin type="com.cd2cd.util.mbg.AnnotationPlugin" />
		<plugin type="com.cd2cd.util.mbg.MysqlPaginationPlugin" />
		
		<!-- xml扩展 -->
		<plugin type="com.cd2cd.util.mbg.MapperExtPlugin" /> 
		<plugin type="org.mybatis.generator.plugins.EqualsHashCodePlugin" />
		
		<commentGenerator type="com.cd2cd.util.mbg.MyCommentGenerator">
			<property name="suppressAllComments" value="true"/>
		</commentGenerator>
		
		
		 <jdbcConnection driverClass="${driverClass}"
			connectionURL="${connectionURL}" userId="${userId}" password="${password}" >
		</jdbcConnection>
		
		<javaModelGenerator targetPackage="${javaModel}" targetProject="${targetProject}src/main/java/" />
		<sqlMapGenerator targetPackage="${sqlMap}" targetProject="${targetProject}src/main/java/" />
		<javaClientGenerator targetPackage="${javaClient}" targetProject="${targetProject}src/main/java/" type="XMLMAPPER" />
		
		<!-- sys table -->
		<#list tables as table>
		
			<table domainObjectName="${table.getJavaTableName()}" tableName="${table.name}" >
				<generatedKey column="id" sqlStatement="MySql" identity="true"/>
			</table>
		
		</#list>
		
	</context>
</generatorConfiguration>