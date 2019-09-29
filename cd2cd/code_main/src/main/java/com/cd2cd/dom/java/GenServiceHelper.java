package com.cd2cd.dom.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cd2cd.dom.java.interfase.InterfaceImplFormat;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;

import com.cd2cd.dom.java.interfase.InterfaceFormat;
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
	
	private String getInterfaceClassName() {
		String type = classFile.getFileClassPath();
		type = replaceEndWord(type);
		return type;
	}
	
	private String getInterfaceClassAbsPath() {
		String filePath = classFile.getFileGenPath();
		filePath = replaceEndWord(filePath);
		return filePath;
	}

	private String getOriginInterfaceCodeTxt() {
		String filePath = getInterfaceClassAbsPath();
		File file = new File(filePath);
		if(file.exists()) {
			try {
				return IOUtils.toString(new FileInputStream(file), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getImplClassName() {
		String type = classFile.getFileClassPath();
		int typeSLen = type.length();

		String implPkg = type.substring(0, type.lastIndexOf(".") - 1) + "impl";
		String implName = type.substring(type.lastIndexOf(".") + 1, typeSLen);
		String newClassName = implPkg+implName;
		type = replaceEndWord(newClassName);
		return type;
	}

	private String getImplClassAbsPath() {
		String filePath = classFile.getFileGenPath();
		int fpLen = filePath.length();
		String implPkg = filePath.substring(0, filePath.lastIndexOf("/") - 1) + "impl";
		String implName = filePath.substring(filePath.lastIndexOf("/") + 1, fpLen);
		String newClassName = implPkg+implName;

		filePath = replaceEndWord(newClassName);
		return filePath;
	}

	private String getOriginImplCodeTxt() {
		String filePath = getImplClassAbsPath();
		File file = new File(filePath);
		if(file.exists()) {
			try {
				return IOUtils.toString(new FileInputStream(file), "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private void genServiceInterface() throws FileNotFoundException, IOException {
		String type = getInterfaceClassName();
		Interface mInterface = new Interface(type);
		mInterface.setVisibility(JavaVisibility.PUBLIC);
		
		if(StringUtils.isNotBlank(file.getComment())) {
			mInterface.addJavaDocLine("/** "+CodeUtils._n + file.getComment() + CodeUtils._n+" **/");
		}
		
		// class import
		addClassImport(mInterface);
		
		// origin code method list
		String originCode = getOriginInterfaceCodeTxt();
		InterfaceFormat iff = new InterfaceFormat(originCode, type);
		Interface originInferface = iff.getmInterface();
		List<MyMethod> originMethods = iff.getMethods();
		Map<String, MyMethod> methodDic = iff.getGenMethodMap();
		
		Map<String, String> newFunGenMap = new HashMap<>();
		
		/** 添加方法 */
		for(ProFun fun : file.getFuns()) {
			
			// not gen service
			if( ! "yes".equals(fun.getGenService())) {
				continue;
			}
			
			Method m = new Method(fun.getFunName());
			
			String genStr = CodeUtils.getFunIdenStr(fun.getId()+"");
			MyMethod originMethod = methodDic.get(genStr);
			
			m.setVisibility(JavaVisibility.PUBLIC);
			m.addJavaDocLine("/**");
			
			// merge add user cusotem comment
			if(originMethod != null && CollectionUtils.isNotEmpty(originMethod.getCustomComment())) {
				m.getJavaDocLines().addAll(originMethod.getCustomComment());
			}
			
			// mark ok
			newFunGenMap.put(genStr, "markOK");
			
			m.addJavaDocLine(" * "+ genStr);
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

		// add origin interface import type
		mInterface.addImportedTypes(originInferface.getImportedTypes());
		
		// add fun for not gen fun in origin 
		for(MyMethod mm :originMethods) {
			
			if(newFunGenMap.get(mm.getGenStr()) == null && StringUtils.isEmpty(mm.getGenStr())) {
				mInterface.addMethod(mm);
			}
		}
		
		if(CollectionUtils.isNotEmpty(mInterface.getMethods())) {
			String classTxt = mInterface.getFormattedContent();
			String filePath = getInterfaceClassAbsPath();
			
			// to write
			writeFile(classTxt, filePath);
		}
	}

	private void addClassImport(CompilationUnit compilationUnit) {
		for(String importedType : file.getImportTypes()) {
			compilationUnit.addImportedType(new FullyQualifiedJavaType(importedType));
		}
	}

	private void writeFile(String classTxt , String filePath) throws FileNotFoundException, IOException {
		File file = new File(filePath);
		if( ! file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		IOUtils.write(classTxt, new FileOutputStream(file), "utf-8");
	}
	
	private void genServiceImpl() {
		String interfaceType = getInterfaceClassName();
		FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(interfaceType);

		String implType = getImplClassName();

		TopLevelClass serviceImpl = new TopLevelClass(implType);
		serviceImpl.addSuperInterface(superInterface);
		serviceImpl.setVisibility(JavaVisibility.PUBLIC);

		/** class import **/
		addClassImport(serviceImpl);

		// origin code method list
		String originCode = getOriginImplCodeTxt();
		InterfaceImplFormat iff = new InterfaceImplFormat(originCode, implType);
		TopLevelClass originImpl = iff.getmTopLevelClass();
		List<MyMethod> originMethods = iff.getMethods();
		Map<String, MyMethod> methodDic = iff.getGenMethodMap();

		Map<String, String> newFunGenMap = new HashMap<>();

		/** super interface */

		/** 添加方法 */
		for(ProFun fun : file.getFuns()) {

			// not gen service
			if (!"yes".equals(fun.getGenService())) {
				continue;
			}

			/** function 注解：事务注释、或其他注解 */

		}


	}
	
	public void genCode() throws FileNotFoundException, IOException {
//		genServiceInterface();
		genServiceImpl();
	}
	
	
}