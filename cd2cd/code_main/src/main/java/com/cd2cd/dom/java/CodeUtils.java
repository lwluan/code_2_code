package com.cd2cd.dom.java;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;

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
		String key = "public interface";
		Pattern p = Pattern.compile("/\\*\\*[\\s\\S]*" + key);
		Matcher m = p.matcher(txt);
		if(m.find()) {
			String cName = m.group();
			cName = cName.substring(0, cName.length() - key.length()).trim();
			cName = cName.trim();
			return cName;
		}
		return null;
	}
	
	public static Map<String, MyMethod> getInterfaceMethods(List<MyMethod> methods, String code) {
		Map<String, MyMethod> mapDic = new HashMap<>();
		if(StringUtils.isBlank(code)) {
			return mapDic;
		}
		
		// 匹配有注释或无注释方法
		Pattern p = Pattern.compile("(.+/\\*(\\s|.)*?[^\\{]\\*/(\\s|.)*?;)|(.+\\(.*\\);)");
		Matcher m = p.matcher(code);
		while(m.find()) {
			String cName = m.group();
			Pattern fp = Pattern.compile(".+;");
			Matcher mp = fp.matcher(cName);
			mp.find();
			String fun = mp.group().trim();
			
			String vis = "public";
			int s = fun.indexOf(vis);
			if(s > 0) {
				s = vis.length();
			} else {
				s = 0;
			}
			String rType = fun.substring(s, fun.indexOf(" "));
			String fName = fun.substring(rType.length(), fun.indexOf("(")).trim();
			String params = fun.substring(fun.indexOf("(")+1, fun.indexOf(")")).trim();
			
			MyMethod method = new MyMethod(fName);
			method.setReturnType(new FullyQualifiedJavaType(rType));
			
			if(methods != null) {
				methods.add(method);
			}
			
			if(StringUtils.isNotBlank(params)) {
				String[] pp = params.split(",");
				for(String ps: pp) {
					String[] pArg = ps.trim().replaceAll("  ", " ").split(" ");
					String paramsType = pArg[0];
					String paramsName = pArg[1];
					Parameter parameter = new Parameter(new FullyQualifiedJavaType(paramsType), paramsName); 
					method.addParameter(parameter);
				}
			}
			
			String javaDocLine = null;
			if(cName.lastIndexOf("/*") > -1) {
				javaDocLine = cName.substring(cName.lastIndexOf("/*"), cName.lastIndexOf("*/")+2);
				method.addJavaDocLine(javaDocLine);
			}
			
			// 唯一标识
			Pattern genP = Pattern.compile("@gen_.*_lwl");
			Matcher GenMp = genP.matcher(cName);
			if(GenMp.find()) {
				String genStr = GenMp.group().trim();
				method.setGenStr(genStr);
				
				// to set map gen
				mapDic.put(genStr, method);
				
				// 自定义 comment
				int si = cName.indexOf(_n);
				int genI = cName.indexOf(genStr);
				if(si < genI) {
					String comment = cName.substring(si+_n.length(), genI);
					if(comment.lastIndexOf(_n) > -1) {
						comment = comment.substring(0, comment.lastIndexOf(_n));
						String[] lines = comment.split(_n);
						
						for(String l: lines) {
							method.getCustomComment().add(" " + l.trim());
						}
					}
				}
			}
		}
		return mapDic;
	}
	
	public static void main(String[] args) throws Exception {
		
		String code = IOUtils.toString(new FileInputStream("/Users/leiwuluan/Documents/java-source/loan_admin/loan_admin_main/src/main/java/com/yishang/loan_admin/credit_trial/service/CreditTrialService.java"), "utf-8");
		getInterfaceMethods(null, code);
	}

}
