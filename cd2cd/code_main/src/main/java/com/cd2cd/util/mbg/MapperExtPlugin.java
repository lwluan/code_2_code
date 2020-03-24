package com.cd2cd.util.mbg;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.XmlFormatter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;


/**
 * mapper xml可扩展<br />
 * 自动生成domain, xml, mapper子类
 * @author leiwuluan
 */
public class MapperExtPlugin extends PluginAdapter {

	private String javaClientGeneratorPackage;
	private String javaClientGeneratorGenPackage;
	private String sqlMapGeneratorPackage;
	private String sqlMapGeneratorGenPackage;
	private String javaModelGeneratorPackage;
	private String javaModelGeneratorGenPackage;
	
	private String SUPER_STR = "Super";
	
	@Override
	public boolean validate(List<String> warnings) {
		this.javaClientGeneratorPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
		this.sqlMapGeneratorPackage = context.getSqlMapGeneratorConfiguration().getTargetPackage();
		this.javaModelGeneratorPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
		
		// = = = 往上推一级 = = = 
		
		javaClientGeneratorGenPackage = javaClientGeneratorPackage + ".gen";
		sqlMapGeneratorGenPackage = sqlMapGeneratorPackage + ".gen";
		javaModelGeneratorGenPackage = javaModelGeneratorPackage + ".gen";
		
		return true;
	}
	
	/**
	 * domain
	 */
	 public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType type = topLevelClass.getType();
		FullyQualifiedJavaType newType = new FullyQualifiedJavaType(javaModelGeneratorGenPackage +"." + SUPER_STR + type.getShortName());
		try {
			Field field = topLevelClass.getClass().getSuperclass().getDeclaredField("type");
			field.setAccessible(true);
			field.set(topLevelClass, newType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	 }

	 /**
	  * 查询类
	  */
	public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		
		FullyQualifiedJavaType type = topLevelClass.getType();
		String packageStr = type.getPackageName() + ".gen." + type.getShortName();
		FullyQualifiedJavaType newType = new FullyQualifiedJavaType(packageStr);
		try {
			Field field = topLevelClass.getClass().getSuperclass().getDeclaredField("type");
			field.setAccessible(true);
			field.set(topLevelClass, newType);
			
			String exampleType = introspectedTable.getExampleType();
			int lastIndex = exampleType.lastIndexOf(".");
			exampleType = exampleType.substring(0, lastIndex) + ".gen"+ exampleType.substring(lastIndex, exampleType.length());
			introspectedTable.setExampleType(exampleType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
    }
	
	/**
	 * Mapper
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		
		FullyQualifiedJavaType type = interfaze.getType();
		FullyQualifiedJavaType newType = new FullyQualifiedJavaType(javaClientGeneratorGenPackage +"." + SUPER_STR + type.getShortName());
		Constants.setObjVal(interfaze, "type", newType);
		return true;
	}
	
	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		
		Constants.setObjVal(sqlMap, "targetPackage", sqlMapGeneratorGenPackage);
		Constants.setObjVal(sqlMap, "fileName", SUPER_STR + sqlMap.getFileName());
		
		// 设置 xml mapper namespace值
		Document document = Constants.getObjVal(sqlMap, "document");
		
		XmlElement rootElement = document.getRootElement();
		Attribute attribute = rootElement.getAttributes().get(0);
		
		FullyQualifiedJavaType daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		String genMapperPath = sqlMapGeneratorGenPackage +"."+ SUPER_STR + daoType.getShortName();
		
		Constants.setObjVal(attribute, "value", genMapperPath);
		
		context.getSqlMapGeneratorConfiguration().setTargetPackage(sqlMapGeneratorGenPackage);
		
		Constants.setObjVal(sqlMap, "isMergeable", false); // xml每次生成复盖
		
        return true;
    }
	
	/**
	 * 返回生成子类的文件
	 */
	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		
		FullyQualifiedJavaType daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
		String fileName = daoType.getFullyQualifiedName();
		String project = context.getJavaClientGeneratorConfiguration().getTargetProject();
		
		List<GeneratedJavaFile> files = new ArrayList<GeneratedJavaFile>();
		
		
		// - - - 自动生成Mapper子类 - - -
		String extFilePath = (project + fileName) .replaceAll("\\.", "/") + ".java";
		if( ! new File(extFilePath).exists()) {
			Interface childInterface = new Interface(daoType);
			childInterface.setVisibility(JavaVisibility.PUBLIC);
			FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(javaClientGeneratorGenPackage +"."+ SUPER_STR + daoType.getShortName());
			childInterface.addImportedType(superInterface);
			childInterface.addSuperInterface(superInterface);
			
			// @Mapper
			FullyQualifiedJavaType mapperAnType = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper");
			childInterface.addImportedType(mapperAnType);
			childInterface.addAnnotation("@Mapper");
			
			JavaFormatter javaFormatter = new DefaultJavaFormatter();
			GeneratedJavaFile file = new GeneratedJavaFile(childInterface, project, javaFormatter);
			files.add(file);
		}
		
		// - - - 自动生成BaseModel子类 - - -
		FullyQualifiedJavaType baseType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		fileName = baseType.getFullyQualifiedName();
		project = context.getJavaModelGeneratorConfiguration().getTargetProject();
		extFilePath = (project + fileName) .replaceAll("\\.", "/") + ".java";
		if( ! new File(extFilePath).exists()) {
			TopLevelClass model = new TopLevelClass(baseType);
			model.setVisibility(JavaVisibility.PUBLIC);
			FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(javaModelGeneratorGenPackage +"."+ SUPER_STR + baseType.getShortName());
			model.addImportedType(superInterface);
			model.setSuperClass(superInterface);
			
			JavaFormatter javaFormatter = new DefaultJavaFormatter();
			GeneratedJavaFile file = new GeneratedJavaFile(model, project, javaFormatter);
			
			// - - - - - 序列化接口 - - - - - 
			FullyQualifiedJavaType serializable = new FullyQualifiedJavaType("java.io.Serializable");
			model.addImportedType(serializable);
			model.addSuperInterface(serializable);

            org.mybatis.generator.api.dom.java.Field field = new org.mybatis.generator.api.dom.java.Field();
            field.setFinal(true);
            field.setInitializationString("1L"); //$NON-NLS-1$
            field.setName("serialVersionUID"); //$NON-NLS-1$
            field.setStatic(true);
            field.setType(new FullyQualifiedJavaType("long")); //$NON-NLS-1$
            field.setVisibility(JavaVisibility.PRIVATE);
            context.getCommentGenerator().addFieldComment(field, introspectedTable);

            model.addField(field);
            
			files.add(file);
		}
		return files;
	}
	
	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
			IntrospectedTable introspectedTable) {
		
		
		String fileName = introspectedTable.getMyBatis3XmlMapperFileName();
		String targetPackage = sqlMapGeneratorPackage;
		String targetProject = context.getSqlMapGeneratorConfiguration().getTargetProject();
		
		// 拷贝mybatis文件
		String xmlMapperPath = (targetProject + targetPackage) .replaceAll("\\.", "/") + "/";
		
		// 自定义文件存在 不自动再生成
		String extFilePath = xmlMapperPath + fileName;
		if( ! new File(extFilePath).exists()) {
			// - - - 自动生成xmlMapper子文件 - - -
			List<GeneratedXmlFile> files = new ArrayList<GeneratedXmlFile>();
			FullyQualifiedJavaType daoType = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
			XmlElement rootElement = new XmlElement("mapper");
			rootElement.addAttribute(new Attribute("namespace", daoType.getFullyQualifiedName()));
			
			Element element = new TextElement("<!-- 自定义sql -->\n\n");
			rootElement.addElement(element);
			
			// 加入自定义文档头 支持extends
			Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
			document.setRootElement(rootElement);
			
			XmlFormatter xmlFormatter = new DefaultXmlFormatter();
			GeneratedXmlFile generatedXmlFile = new GeneratedXmlFile(document, fileName, targetPackage, targetProject, false, xmlFormatter);
			
			files.add(generatedXmlFile);
			
			return files;
		}
		
		
		return null;
	}

}
