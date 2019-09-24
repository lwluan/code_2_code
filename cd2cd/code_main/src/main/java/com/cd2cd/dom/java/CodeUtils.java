package com.cd2cd.dom.java;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import com.cd2cd.dom.java.TypeEnum.CollectionType;

public class CodeUtils {
	public static String _n = System.getProperty("line.separator");
	public static String typeByCollectionType(String type, String collectionType) {
		if(CollectionType.list.name().equalsIgnoreCase(collectionType)) {
			return type = "List<" + type + ">";
		} else if(CollectionType.map.name().equalsIgnoreCase(collectionType)) {
			return type = "Map<String, " + type + ">";
		} else if(CollectionType.set.name().equalsIgnoreCase(collectionType)) {
			return type = "Set<" + type + ">";
		}
		return type;
	}
	
	public static String getFunIdenStr(String funId) {
		return "@gen_"+funId+"_lwl";
	}
	
	public static String getFunHeaderByIdentify(String str, String iden) {
		
		String eIden = ") {";
		int i = str.indexOf(iden);
		
		String endTmpStr = str.substring(i + iden.length());
		int funEndI = endTmpStr.indexOf(eIden);
		if(funEndI < 0) {
			funEndI = endTmpStr.indexOf("){");
		}
		String endStr = endTmpStr.substring(0, funEndI + eIden.length() + 1);
		
		return endStr;
	}
	
	public static int findFunEndIndex(String cc) {
		int i1 = 1;
		for(int i=0; i<cc.length(); i++) {
			if(cc.charAt(i) == '{') {
				i1 ++;
			} else if(cc.charAt(i) == '}') {
				i1 --;
			}
			if(i1 == 0) {
				return i;
			}
		}
		return -1;
	}
	
	public static String updateClassImport(String classTxt, Set<String> newImports) {
		
		String[] lines = classTxt.split(_n);
		
		Set<String> imports = new TreeSet<>();
		String sImport = null;
		String eImport = null;
		for(String line: lines) {
			
			int ii = line.indexOf("import");
			if(ii > -1 && ii < 5) {
				if(sImport == null) {
					sImport = line;
				}
				eImport = line;
				imports.add(line);
			}
			if(line.indexOf("public class") > -1) {
				break;
			}
		}
		
		imports.addAll(newImports);
		
		String csStr = classTxt.substring(0, classTxt.indexOf(sImport));
		String ceStr = classTxt.substring(classTxt.indexOf(eImport) + eImport.length(), classTxt.length());
		
		return csStr + String.join(_n, imports) +ceStr;
	}
	
	public static String getInterfaceName(String txt) {
		Pattern p = Pattern.compile("public\\s+interface\\s+.+\\{");
		Matcher m = p.matcher(txt);
		if(m.find()) {
			String cName = m.group();
			cName = cName.split("interface")[1];
			cName = cName.substring(0, cName.indexOf("{"));
			cName = cName.trim();
			return cName;
		}
		return null;
	}
	
	public static String getClassName(String txt) {
		Pattern p = Pattern.compile("public\\s+class\\s+.+\\{");
		Matcher m = p.matcher(txt);
		if(m.find()) {
			String cName = m.group();
			cName = cName.split("class")[1];
			cName = cName.substring(0, cName.indexOf("{"));
			cName = cName.trim();
			return cName;
		}
		return null;
	}
	
	public static String getPackage(String txt) {
		String pStr = "package";
		Pattern p = Pattern.compile(pStr+"\\s+.+;");
		Matcher m = p.matcher(txt);
		if(m.find()) {
			String cName = m.group();
			cName = cName.substring(cName.indexOf(pStr)+pStr.length(), cName.indexOf(";"));
			cName = cName.trim();
			return cName;
		}
		return null;
	}
	
	public static Set<FullyQualifiedJavaType> getImport(String txt) {
		Set<FullyQualifiedJavaType> importedTypes = new HashSet<>();
		
		String pStr = "import";
		Pattern p = Pattern.compile(pStr+"\\s+.+;");
		Matcher m = p.matcher(txt);
		while(m.find()) {
			String s = m.group();
			s = s.substring(s.indexOf(pStr)+pStr.length(), s.indexOf(";"));
			s = s.trim();
			FullyQualifiedJavaType type = new FullyQualifiedJavaType(s);
			importedTypes.add(type);
		}
		return importedTypes;
	}
	
	public static String getClassjavaDocLine(String txt) {
		Pattern p = Pattern.compile("\\s+\\/\\*\\*.+public interface");
		Matcher m = p.matcher(txt);
		if(m.find()) {
			String cName = m.group();
			
			System.out.println(cName);
			
			cName = cName.trim();
			return cName;
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		String s = "99999  package com.cd2cd.dom.java;  ooo";
		System.out.println(getPackage(s));

	}
}
