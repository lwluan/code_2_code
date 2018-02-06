package com.cd2cd.util;

public enum FileTypeEnum {
	controller, service, vo, mapper, domain;
	
	public boolean eq(String name) {
		return this.name().equalsIgnoreCase(name); 
	}
}
