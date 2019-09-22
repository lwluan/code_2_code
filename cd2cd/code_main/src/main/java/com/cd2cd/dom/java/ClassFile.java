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

	public ClassFile(String fileGenPath, String fileClassPath, String moduleName, String packageType, String pkgName, String className) {
		super();
		this.fileGenPath = fileGenPath;
		this.fileClassPath = fileClassPath;
		this.packageType = packageType;
		this.pkgName = pkgName;
		this.className = className;
		this.moduleName = moduleName;
	}

	public String getFileGenPackagePath() {
		String fgp = null;
		if (ProjectModulTypeEnum.standard.name().equals(packageType)) {
			fgp = String.format("%s/%s", fileGenPath, pkgName);
		} else { // 模块化
			fgp = String.format("%s/%s/%s", fileGenPath, this.moduleName, pkgName);
		}
		return fgp.replaceAll("//", "/");
	}
	
	public String getFileGenPath() {
		return String.format("%s/%s.java", getFileGenPackagePath(), className);
	}

	public String getFileClassPackagePath() {
		String fcp = null;
		if (ProjectModulTypeEnum.standard.name().equals(packageType)) {
			fcp = String.format("%s.%s", fileClassPath, pkgName);
		} else { // 模块化
			fcp = String.format("%s.%s.%s", fileClassPath, moduleName, pkgName);
		}
		return fcp;
	}
	
	public String getFileClassPath() {
		return String.format("%s.%s", getFileClassPackagePath(), className);
	}

	public TopLevelClass getType() {
		if(this.type == null) {
			FullyQualifiedJavaType controllerType = new FullyQualifiedJavaType(getFileClassPath());
			TopLevelClass topClass = new TopLevelClass(controllerType);
			this.type = topClass; 
		}
		return this.type;
	}

	public String getPackageType() {
		return this.packageType;
	}

	public String getPkgName() {
		return this.pkgName;
	}

	public String getClassName() {
		return this.className;
	}

	public String getModuleName() {
		return this.moduleName;
	}

}
