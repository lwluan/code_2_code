package com.cd2cd.dom.java.interfase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;

import com.cd2cd.dom.java.CodeUtils;
import com.cd2cd.dom.java.MyMethod;

public class InterfaceFormat {
	private List<MyMethod> methods = new ArrayList<>();
	private Interface mInterface;
	private Map<String, MyMethod> genMethodMap = new HashMap<>();
	public InterfaceFormat(String code, String defaultName) {
		
		if(StringUtils.isEmpty(code)) {
			mInterface = new Interface(defaultName);
			return;
		}
		
		// 生成包名、类名
		String pkg = CodeUtils.getPackage(code);
		String cName = CodeUtils.getInterfaceName(code);
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("%s.%s", pkg, cName));
		mInterface = new Interface(type);
		mInterface.setVisibility(JavaVisibility.PUBLIC);
		
		// 设置类的import 
		Set<FullyQualifiedJavaType> importedTypes = CodeUtils.getImport(code);
		mInterface.addImportedTypes(importedTypes);
		
		// 设置类注释
		String javaDocLine = CodeUtils.getClassjavaDocLine(code);
		if(StringUtils.isNotBlank(javaDocLine)) {
			mInterface.addJavaDocLine(javaDocLine);
		}
		
		// 类的方法
		genMethodMap = CodeUtils.getInterfaceMethods(methods, code);
		mInterface.getMethods().addAll(methods);
	}
	
	public List<MyMethod> getMethods() {
		return methods;
	}

	public Interface getmInterface() {
		return mInterface;
	}

	public Map<String, MyMethod> getGenMethodMap() {
		return genMethodMap;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		String code = IOUtils.toString(new FileInputStream("/Users/leiwuluan/Documents/java-source/loan_admin/loan_admin_main/src/main/java/com/yishang/loan_admin/credit_trial/service/CreditTrialService.java"), "utf-8");
		
		InterfaceFormat mInterfaceFormat = new InterfaceFormat(code, "aaa");
		System.out.println(mInterfaceFormat.getmInterface().getFormattedContent());
		
	}

}
