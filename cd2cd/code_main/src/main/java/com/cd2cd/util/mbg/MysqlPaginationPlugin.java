package com.cd2cd.util.mbg;

import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 */
public class MysqlPaginationPlugin extends PluginAdapter {

	@Override  
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {  
        // add field, getter, setter for limit clause  
        addLimit(topLevelClass, introspectedTable, "mysqlOffset");  
        addLimit(topLevelClass, introspectedTable, "mysqlLength");  
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);  
    }  
    @Override  
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(  
            XmlElement element, IntrospectedTable introspectedTable) {  
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$  
        isNotNullElement.addAttribute(new Attribute("test", "mysqlOffset != null and mysqlLength != null")); //$NON-NLS-1$ //$NON-NLS-2$  
        isNotNullElement.addElement(new TextElement("<![CDATA[ limit #{mysqlOffset} , #{mysqlLength} ]]>"));  
        element.addElement(isNotNullElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);  
    } 
    
    private void addLimit(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, String name) {  
        CommentGenerator commentGenerator = context.getCommentGenerator();  
        Field field = new Field();  
        field.setVisibility(JavaVisibility.PROTECTED);  
        field.setType(new FullyQualifiedJavaType("Integer"));  
        field.setName(name);  
        commentGenerator.addFieldComment(field, introspectedTable);  
        topLevelClass.addField(field);
        
        char c = name.charAt(0);  
        String camel = Character.toUpperCase(c) + name.substring(1);  
        Method method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setName("set" + camel);  
        method.addParameter(new Parameter(new FullyQualifiedJavaType("Integer"), name));
        
        
        addSetterComment(method, Constants.lineSeparator+"\t *            指定返回记录行的偏移量<br>"+ Constants.lineSeparator+"\t *            " 
        + "mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15", "mysqlOffset");
        method.addBodyLine("this." + name + "=" + name + ";");  
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
        method = new Method();  
        method.setVisibility(JavaVisibility.PUBLIC);  
        method.setReturnType(new FullyQualifiedJavaType("Integer"));  
        method.setName("get" + camel);  
        addSetterComment(method, Constants.lineSeparator+"\t *            指定返回记录行的最大数目"
				+ "<br>"+Constants.lineSeparator+"\t *            mysqlOffset= 5,mysqlLength=10;  // 检索记录行 6-15", "mysqlLength");
        method.addBodyLine("return " + name + ";");  
        
        commentGenerator.addGeneralMethodComment(method, introspectedTable);  
        topLevelClass.addMethod(method);  
    }  
    
    public boolean validate(List<String> warnings) {  
        return true;  
    } 

    
    public void addSetterComment(Method method, String doc, String paramName) {
		StringBuilder sb = new StringBuilder();
		if (doc != null && doc.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * @param " + paramName + " ");
			sb.append(doc);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */");
		}
	}

	public void addClassComment(InnerClass innerClass, String doc) {
		StringBuilder sb = new StringBuilder();
		String remarks = doc;
		if (remarks != null && remarks.length() != 0) {
			innerClass.addJavaDocLine("/**");
			sb.append(" * " + remarks.replaceAll(Constants.lineSeparator, "<br>" + Constants.lineSeparator + " * "));
			innerClass.addJavaDocLine(sb.toString());
			innerClass.addJavaDocLine(" */");
		}
	}

	public void addFieldComment(Field field, String doc) {
		StringBuilder sb = new StringBuilder();
		if (doc != null && doc.length() != 0) {
			field.addJavaDocLine("/**");
			sb.append(" * ");
			doc = doc.replaceAll(Constants.lineSeparator, "<br>" + Constants.lineSeparator + "\t * ");
			sb.append(doc);
			field.addJavaDocLine(sb.toString());
			field.addJavaDocLine(" */");
		}
	}

	public void addGetterComment(Method method, String doc, String paramName) {
		StringBuilder sb = new StringBuilder();
		if (doc != null && doc.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * @return ");
			doc = doc.replaceAll(Constants.lineSeparator, "<br>" + Constants.lineSeparator + "\t * ");
			sb.append(doc);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */");
		}
	}

	public void addMethodComment(Method method, String doc, String paramName) {
		StringBuilder sb = new StringBuilder();
		if (doc != null && doc.length() != 0) {
			method.addJavaDocLine("/**");
			sb.append(" * ");
			doc = doc.replaceAll(Constants.lineSeparator, "<br>" + Constants.lineSeparator + "\t * ");
			sb.append(doc);
			method.addJavaDocLine(sb.toString());
			method.addJavaDocLine(" */");
		}
	}
}
