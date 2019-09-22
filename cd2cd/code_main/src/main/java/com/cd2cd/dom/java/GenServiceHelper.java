package com.cd2cd.dom.java;

import org.mybatis.generator.api.dom.java.InnerInterface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.cd2cd.domain.ProFile;

public class GenServiceHelper {

	private ClassFile classFile;
	private ProFile file;
	public GenServiceHelper(ClassFile classFile, ProFile file) {
		super();
		this.classFile = classFile;
		this.file = file;
	}
	
	private void genServiceInterface() {
		String type = classFile.getFileClassPath();
		InnerInterface mInnerInterface = new InnerInterface(type);
		
		/** 添加方法 */
		
		// gen
		classFile.getFileClassPath();
		
	}
	
	private void genServiceImpl() {
		String fileGenPath = classFile.getFileGenPath();
		TopLevelClass topClass = classFile.getType();
	}
	
	public void genCode() {
		
		
		
		genServiceInterface();
		genServiceImpl();
	}
	
	
}
