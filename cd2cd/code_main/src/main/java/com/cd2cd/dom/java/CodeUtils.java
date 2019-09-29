package com.cd2cd.dom.java;

import java.io.FileInputStream;
import java.util.*;
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

	/**
	 *
	 * @param type interface class
	 * @param txt
	 * @return
	 */
	public static String getClassjavaDocLine(String type, String txt) {
		String key = "public "+type;
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

	private static int lastEndIndex(String s) {
		int e1 = s.lastIndexOf("}");
		int e2 = s.lastIndexOf(";");
		String tmp = s;

		while(tmp.substring(e1 +1, tmp.length()).trim().startsWith("*")) {
			tmp = s.substring(0, e1);
			e1 = tmp.lastIndexOf("}");
		}

		tmp = s;
		while(tmp.substring(e2 +1, tmp.length()).trim().startsWith("*")) {
			tmp = s.substring(0, e2);
			e2 = tmp.lastIndexOf(";");
		}

		int i = 0;
		if(e1 > e2) {
			i = e1;
		} else {
			i = e2;
		}
		return i + 1;
	}

	public static Map<String, MyMethod> getInterfaceImplMethods(List<MyMethod> methods, String code) {
		Map<String, MyMethod> mapDic = new HashMap<>();
		if(StringUtils.isBlank(code)) {
			return mapDic;
		}

		// 匹配有注释或无注释方法
		Pattern p = Pattern.compile(".*(public|private|protected){1}.{1,3}(static)*.*\\(.*\\)+[\\s]+?\\{[\\s|\\S]+?(public|private|protected|@Override){1}");
		Matcher m = p.matcher(code);
		List<String> methodStrs = new ArrayList<>();
		int sIndex = 0;
		int eIndex = 0;
		while(m.find()) {
			String cName = m.group();

			// 获取方法前缀内容；注解、注释(多行，多段)
			String methodHeader = code.substring(sIndex, m.start());

			int mSindex = lastEndIndex(methodHeader);
			methodHeader = code.substring(mSindex, m.start());

			// 有可能注释存在 } 字符
			int lastNum = cName.lastIndexOf("}");

			String tmp = cName;
			while(tmp.substring(lastNum +1, tmp.length()).trim().startsWith("*")) {
				tmp = cName.substring(0, lastNum);
				lastNum = tmp.lastIndexOf("}");
			}
			cName = cName.substring(0, lastNum+1);
			String mStr = methodHeader + cName;
			methodStrs.add(mStr);
			if(StringUtils.isNotBlank(cName)) continue;

			sIndex = m.start();
		}

		// 加入最后一个方法



		for (String cName: methodStrs) {
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
		
		String code = IOUtils.toString(new FileInputStream("/Users/leiwuluan/Documents/java-source/loan_admin/loan_admin_main/src/main/java/com/yishang/loan_admin/credit_trial/service/impl/CreditTrialServiceImpl.java"), "utf-8");
		getInterfaceImplMethods(null, code);

	}

}
