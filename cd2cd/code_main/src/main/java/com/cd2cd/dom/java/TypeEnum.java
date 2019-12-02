package com.cd2cd.dom.java;

public class TypeEnum {

	public enum FileTypeEnum {
		controller, service, vo, mapper, domain;
		
		public boolean eq(String name) {
			return this.name().equalsIgnoreCase(name); 
		}
	}
	
	public enum ProjectModulTypeEnum {
		standard, module
	}
	
	public enum PackageTypeEnum {
		Flat,
		Hierarchical;
	}

	public enum FunArgType {
		base,vo,pojo
	}
	
	public enum FieldDataType {
		base, vo, T
	}
	
	public enum CollectionType {
		single, list, set, map
	}
}
