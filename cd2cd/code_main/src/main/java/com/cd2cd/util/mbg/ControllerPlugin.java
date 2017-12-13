package com.cd2cd.util.mbg;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;

public class ControllerPlugin extends PluginAdapter {
	
	private FullyQualifiedJavaType slf4jLogger;
	private FullyQualifiedJavaType slf4jLoggerFactory;
	
	private List<Method> methods;
	
	public ControllerPlugin() {
		super();
		slf4jLogger = new FullyQualifiedJavaType("org.slf4j.Logger");
		slf4jLoggerFactory = new FullyQualifiedJavaType("org.slf4j.LoggerFactory");
		methods = new ArrayList<Method>();
	}
	
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}
	
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		String table = introspectedTable.getBaseRecordType();
		
		
//		FullyQualifiedJavaType genTopLevelClass = new FullyQualifiedJavaType(genPackage + ".Super" + tableName + "ServiceImpl");
//		TopLevelClass topLevelClass = new TopLevelClass(genTopLevelClass);
		
		
		return files;
	}

}
