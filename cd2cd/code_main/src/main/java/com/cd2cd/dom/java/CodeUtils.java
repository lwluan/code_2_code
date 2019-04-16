package com.cd2cd.dom.java;

import com.cd2cd.dom.java.TypeEnum.CollectionType;

public class CodeUtils {

	public static String typeByCollectionType(String type, String collectionType) {
		if(CollectionType.list.name().equalsIgnoreCase(collectionType)) {
			return type = "List<" + type + ">";
		} else if(CollectionType.map.name().equalsIgnoreCase(collectionType)) {
			return type = "Map<String, " + type + ">";
		} else if(CollectionType.set.name().equalsIgnoreCase(collectionType)) {
			return type = "Set<String, " + type + ">";
		}
		return type;
	}
}
