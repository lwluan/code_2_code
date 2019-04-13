package com.cd2cd.dom.java;

public class TypeEnum {

	public static enum FileTypeEnum {
		controller, service, vo, mapper, domain;
		
		public boolean eq(String name) {
			return this.name().equalsIgnoreCase(name); 
		}
	}
	
	public static enum PackageTypeEnum {
		Flat,
		Hierarchical;
	}

	public static enum FunArgType {
		base,vo,pojo
	}
	
	public static enum FieldDataType {
		base, vo, T
	}
	
	public static enum CollectionType {
		single, list, set, map
	}
}
