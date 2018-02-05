package com.cd2cd.admin.util.mbg;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

/**
 */
public class MyCommentGenerator extends DefaultCommentGenerator {
	
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
	    if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
	        field.addJavaDocLine("/**");
	        field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
	        field.addJavaDocLine(" */");
	    }
	}
	
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		
	}
	
	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
			method.addJavaDocLine("/**");
			method.addJavaDocLine(" * " + introspectedColumn.getRemarks());
			method.addJavaDocLine(" */");
	    }
	}
	
	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		this.addSetterComment(method, introspectedTable, introspectedColumn);
	}
	
	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) { }
	
	@Override
	public void addComment(XmlElement xmlElement) {  }

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) { }
	
}
