package com.cd2cd.dom.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import com.cd2cd.dom.java.interfase.InterfaceImplFormat;
import com.cd2cd.util.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;

import com.cd2cd.dom.java.interfase.InterfaceFormat;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.ProFunArg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

public class GenServiceHelper {

	private Logger log = LoggerFactory.getLogger(GenServiceHelper.class);
	private ClassFile classFile;
	private ProFile file;
	public GenServiceHelper(ClassFile classFile, ProFile file) {
		super();
		this.classFile = classFile;
		this.file = file;
	}
	
	private String replaceEndWord(String s, boolean impl) {
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
		String implStr = impl ? "Impl" : "";
		if(null != lastWord) {
			return s.substring(0, s.lastIndexOf(lastWord)) + "Service" +implStr+ endWord;
		} else {
			return s + "Service" +implStr+ endWord;
		}
	}
	
	private String getInterfaceClassName() {
		String type = classFile.getFileClassPath();
		type = replaceEndWord(type, false);
		return type;
	}
	
	private String getInterfaceClassAbsPath() {
		String filePath = classFile.getFileGenPath();
		filePath = replaceEndWord(filePath, false);
		return filePath;
	}

	private String getImplClassName() {
		String type = classFile.getFileClassPath();
		int typeSLen = type.length();

		String implPkg = type.substring(0, type.lastIndexOf(".") + 1) + "impl";
		String implName = type.substring(type.lastIndexOf("."), typeSLen);
		String newClassName = implPkg+implName;
		type = replaceEndWord(newClassName, true);
		return type;
	}

	private String getImplClassAbsPath() {
		String filePath = classFile.getFileGenPath();
		int fpLen = filePath.length();
		String implPkg = filePath.substring(0, filePath.lastIndexOf("/")+1) + "impl";
		String implName = filePath.substring(filePath.lastIndexOf("/"), fpLen);
		String newClassName = implPkg+implName;
		filePath = replaceEndWord(newClassName, true);
		return filePath;
	}

	private String getOriginCodeTxt(String filePath) {
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



	private void genServiceInterface() throws IOException {
		String type = getInterfaceClassName();

		log.info("type={}", type);

		Interface mInterface = new Interface(type);
		mInterface.setVisibility(JavaVisibility.PUBLIC);
		
		if(StringUtils.isNotBlank(file.getComment())) {
			mInterface.addJavaDocLine("/** "+CodeUtils._n + file.getComment() + CodeUtils._n+" **/");
		}
		
		// class import
		addClassImport(mInterface);
		
		// origin code method list
		String originCode = getOriginCodeTxt(getInterfaceClassAbsPath());
		InterfaceFormat iff = new InterfaceFormat(originCode, type);
		Interface originInferface = iff.getmInterface();

		if(originInferface.getMethods().size() < 1) {
			return;
		}

		List<MyMethod> originMethods = iff.getMethods();
		Map<String, MyMethod> oldMethodDic = iff.getGenMethodMap();
		Map<String, Method> newFunGenMap = new HashMap<>();

		// 设置方法
		setFunction(mInterface.getMethods(), oldMethodDic, newFunGenMap, false);

		// add origin interface import type
		mInterface.addImportedTypes(originInferface.getImportedTypes());
		
		// add fun for not gen fun in origin 
		for(MyMethod mm :originMethods) {
			
			if(newFunGenMap.get(mm.getGenStr()) == null && StringUtils.isEmpty(mm.getGenStr())) {
				mInterface.addMethod(mm);
			}
		}

		writeCodeToFile(mInterface, getInterfaceClassAbsPath());

	}

	private void writeCodeToFile(CompilationUnit compilationUnit, String filePath) throws IOException {
		String classTxt = compilationUnit.getFormattedContent();
		// to write
		writeFile(classTxt, filePath);

	}

	private void addClassImport(CompilationUnit compilationUnit) {
		for(String importedType : file.getImportTypes()) {
			compilationUnit.addImportedType(new FullyQualifiedJavaType(importedType));
		}
	}

	private void writeFile(String classTxt, String filePath) throws IOException {
		File file = new File(filePath);
		if( ! file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		IOUtils.write(classTxt, new FileOutputStream(file), "utf-8");
	}

	/**
	 * 源类文件和现有文件混合
	 * @param methods
	 * @param oldMethodDic
	 * @param newFunGenMap
	 * @param impl
	 */
	private void setFunction(List<Method> methods, Map<String, MyMethod> oldMethodDic, Map<String, Method> newFunGenMap, boolean impl) {
		/** 添加方法 */
		for(ProFun fun : file.getFuns()) {

			// not gen service
			if( ! "true".equals(fun.getGenService())) {
				continue;
			}

			Method m = new Method(fun.getFunName());

			String genStr = CodeUtils.getFunIdenStr(fun.getId()+"");
			MyMethod originMethod = oldMethodDic.get(genStr);

			m.setVisibility(JavaVisibility.PUBLIC);
			m.addJavaDocLine("/**");

			// merge add user cusotem comment
			if(originMethod != null && CollectionUtils.isNotEmpty(originMethod.getCustomComment())) {
				m.getJavaDocLines().addAll(originMethod.getCustomComment());
			}

			// mark ok
			newFunGenMap.put(genStr, m);

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

			if(impl) {
				if(null == m.getReturnType()) {
					m.addBodyLine("");
				} else {
					m.addBodyLine("return null;");
				}
			}
			methods.add(m);
		}
	}

	private void genServiceImpl() throws IOException {
		String interfaceType = getInterfaceClassName();
		FullyQualifiedJavaType superInterface = new FullyQualifiedJavaType(interfaceType);

		String implType = getImplClassName();

		TopLevelClass newServiceImpl = new TopLevelClass(implType);

		/** super interface */
		newServiceImpl.addSuperInterface(superInterface);
		newServiceImpl.setVisibility(JavaVisibility.PUBLIC);

		/** class import **/
		addClassImport(newServiceImpl);

		// origin code method list
		String originCode = getOriginCodeTxt(getImplClassAbsPath());
		InterfaceImplFormat iff = new InterfaceImplFormat(originCode, implType);
		TopLevelClass originImpl = iff.getmTopLevelClass();
		addClassImport(originImpl);

		if(originImpl.getMethods().size() <1) {
			return;
		}

		Map<String, MyMethod> oldMethodDic = iff.getGenMethodMap();
		Map<String, Method> newFunGenMap = new HashMap<>();

		// 设置方法
		setFunction(newServiceImpl.getMethods(), oldMethodDic, newFunGenMap, true);


		for(String mKey: newFunGenMap.keySet()) {

			MyMethod oldMethod = oldMethodDic.get(mKey);
			if(Objects.isNull(oldMethod)) {
				originImpl.addMethod(newFunGenMap.get(mKey));
				continue;
			}

			List<String> javaDocLines = new ArrayList<>(oldMethod.getJavaDocLines());
			List<String> bodyLines = new ArrayList<>(oldMethod.getBodyLines());

			BeanUtils.copyProperties(newFunGenMap.get(mKey), oldMethod);

			oldMethod.getJavaDocLines().clear();
			oldMethod.getJavaDocLines().addAll(javaDocLines);

			oldMethod.getBodyLines().clear();
			oldMethod.getBodyLines().addAll(bodyLines);

		}

		originImpl.addAnnotation("@Service");
		originImpl.addImportedType("org.springframework.stereotype.Service");
		originImpl.addImportedType(getInterfaceClassName());
		originImpl.addSuperInterface(new FullyQualifiedJavaType(getInterfaceClassName()));
		writeCodeToFile(originImpl, getImplClassAbsPath());

	}
	
	public void genCode() throws IOException {

		// 判断是否需存在生成 genService
//		genServiceInterface();
//		genServiceImpl();
	}
	
	
}