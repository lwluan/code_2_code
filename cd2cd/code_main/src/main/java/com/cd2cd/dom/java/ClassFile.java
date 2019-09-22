package com.cd2cd.dom.java;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.cd2cd.dom.java.TypeEnum.ProjectModulTypeEnum;

public class ClassFile {

	private String fileGenPath;
	private String fileClassPath;
	private TopLevelClass type;

	private String moduleName; // 模块名称
	private String packageType;
	private String pkgName; // controller, service, vo
	private String className;

	public ClassFile(String fileGenPath, String fileClassPath, String moduldName, String packageType, String pkgName, String className) {
		super();
		this.fileGenPath = fileGenPath;
		this.fileClassPath = fileClassPath;
		this.packageType = packageType;
		this.pkgName = pkgName;
		this.className = className;
		this.moduleName = moduleName;
	}

	public String getFileGenPath() {
		String fgp = null;
		if (ProjectModulTypeEnum.standard.name().equals(packageType)) {
			fgp = String.format("%s/%s/%s.java", fileGenPath, pkgName, className);
		} else { // 模块化
			fgp = String.format("%s/%s/%s/%s.java", fileGenPath, moduleName, pkgName, className);
		}
		return fgp;
	}

	public String getFileClassPath() {
		String fcp = null;
		if (ProjectModulTypeEnum.standard.name().equals(packageType)) {
			fcp = String.format("%s.%s.%s", fileClassPath, pkgName, className);
		} else { // 模块化
			fcp = String.format("%s.%s.%s.%s", fileClassPath, moduleName, pkgName, className);
		}
		return fcp;
	}

	public TopLevelClass getType() {
		FullyQualifiedJavaType controllerType = new FullyQualifiedJavaType(getFileClassPath());
		TopLevelClass topClass = new TopLevelClass(controllerType);
		return topClass;
	}

	public String getPackageType() {
		return packageType;
	}

	public String getPkgName() {
		return pkgName;
	}

	public String getClassName() {
		return className;
	}

	public String getModuleName() {
		return moduleName;
	}

}
