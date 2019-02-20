package com.cd2cd.admin.util.mbg;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class AnnotationPlugin extends PluginAdapter {
	private FullyQualifiedJavaType mapper;

	public AnnotationPlugin() {
		super();
		mapper = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper");
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		interfaze.addAnnotation("@Mapper");
		interfaze.addImportedType(mapper);
		return true;
	}
}
