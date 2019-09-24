package com.cd2cd.dom.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;

public class InterfaceFormat {

	public static Interface toInterface(String code) {
		
		// 生成包名、类名
		String pkg = CodeUtils.getPackage(code);
		String cName = CodeUtils.getInterfaceName(code);
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("%s.%s", pkg, cName));
		Interface i = new Interface(type);
		i.setVisibility(JavaVisibility.PUBLIC);
		
		// 设置类的import 
		Set<FullyQualifiedJavaType> importedTypes = CodeUtils.getImport(code);
		i.addImportedTypes(importedTypes);
		
		// 设置类注释
		String javaDocLine = CodeUtils.getClassjavaDocLine(code);
		i.addJavaDocLine(javaDocLine);
		
		// 类的方法
		
		// 类的唯一标识号
		
		
		return i;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String code = IOUtils.toString(new FileInputStream("/Users/lwl/Documents/source-code/java-code/code_manager/auto_code/auto_code_main/src/main/java/com/cd2cd/auto_code/project/service/ProjectService.java"), "utf-8");
		
		System.out.println(toInterface(code).getFormattedContent());
		
	}

}
