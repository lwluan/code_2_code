package com.cd2cd.dom.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.ProFunArg;

public class GenServiceHelper {

	private ClassFile classFile;
	private ProFile file;
	public GenServiceHelper(ClassFile classFile, ProFile file) {
		super();
		this.classFile = classFile;
		this.file = file;
	}
	
	private String replaceEndWord(String s) {
		String lastWord = null;
		if(s.endsWith("Controller")) {
			lastWord = "Controller";
		} else if(s.endsWith("Rest")) {
			lastWord = "Rest";
		} else if(s.endsWith("Controller.java")) {
			lastWord = "Controller.java";
		} else if(s.endsWith("Rest.java")) {
			lastWord = "Rest.java";
		}
		
		String endWord = s.endsWith(".java") ? ".java" : "";
		
		if(null != lastWord) {
			return s.substring(0, s.lastIndexOf(lastWord)) + "Service" + endWord;
		} else {
			return s + "Service" + endWord;
		}
	}
	
	private String getClassName() {
		String type = classFile.getFileClassPath();
		type = replaceEndWord(type);
		return type;
	}
	
	private String getClassAbsPath() {
		String filePath = classFile.getFileGenPath();
		filePath = replaceEndWord(filePath);
		return filePath;
	}
	
	private void genServiceInterface() throws FileNotFoundException, IOException {
		String type = getClassName();
		Interface mInterface = new Interface(type);
		mInterface.setVisibility(JavaVisibility.PUBLIC);
		
		if(StringUtils.isNotBlank(file.getComment())) {
			mInterface.addJavaDocLine("/** "+CodeUtils._n + file.getComment() + CodeUtils._n+" **/");
		}
		
		// class import
		for(String importedType : file.getImportTypes()) {
			mInterface.addImportedType(new FullyQualifiedJavaType(importedType));
		}
		
		// package
		
		/** 添加方法 */
		for(ProFun fun : file.getFuns()) {
			Method m = new Method(fun.getFunName());
			
			m.setVisibility(JavaVisibility.PUBLIC);
			m.addJavaDocLine("/**");
			m.addJavaDocLine(" * "+ CodeUtils.getFunIdenStr(fun.getId()+""));
			m.addJavaDocLine(" * "+fun.getName());
			m.addJavaDocLine(" * "+fun.getComment());
			
			/**
			 * 方法参数
			 */
			List<ProFunArg> args = fun.getArgs();
			for(ProFunArg arg : args) {
				
				String paramName = arg.getName();
				paramName = paramName.replace("{", "").replace("}", "");
				Parameter mp = new Parameter(new FullyQualifiedJavaType(arg.getArgTypeName()), paramName);
				
				m.addJavaDocLine(" * @param "+paramName);
				
				m.addParameter(mp);
			}
			
			/**
			 * 方法返回值
			 */
			if(StringUtils.isNotBlank(fun.getReturnShow())) {
				m.setReturnType(new FullyQualifiedJavaType(fun.getReturnShow()));
			}
			m.addJavaDocLine("**/");
			mInterface.addMethod(m);
		}
		
		// gen
		String classTxt = mInterface.getFormattedContent();
		String filePath = getClassAbsPath();		
		writeFile(classTxt, filePath);
	}
	
	private void writeFile(String classTxt , String filePath) throws FileNotFoundException, IOException {
		File file = new File(filePath);
		if( ! file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		IOUtils.write(classTxt, new FileOutputStream(file), "utf-8");
	}
	
	private void genServiceImpl() {
		String fileGenPath = classFile.getFileGenPath();
		TopLevelClass topClass = classFile.getType();
	}
	
	public void genCode() throws FileNotFoundException, IOException {
		genServiceInterface();
		genServiceImpl();
	}
	
	
}
