package com.cd2cd.dom.java;

import java.io.FileInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;

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
			cName = InnerClassUtil.getInnerClassName(cName);
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

	public static int lastEndIndex(String s) {
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

	public static Map<String, MyMethod> getInterfaceImplMethodsAndSetClassProperties(TopLevelClass topLevelClass, List<MyMethod> methods, String code) {
		Map<String, MyMethod> mapDic = new HashMap<>();
		if(StringUtils.isBlank(code)) {
			return mapDic;
		}

		code = code.substring(code.indexOf("{") + 1, code.lastIndexOf("}"));
		Stack<Character> stack = new Stack<>();

		List<String> methodStrs = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		boolean firstFun = true;
		char[] codeArr = code.toCharArray();
		for(int i=0; i<codeArr.length; i++) {
			Character c = codeArr[i];
			sb.append(c);
			if('{' == c) {
				if(firstFun && stack.isEmpty()) {
					sb.delete(0, sb.length());

					// 获取first fun comment and fun define
					String methodHeader = code.substring(0, i+1);

					int mSindex = lastEndIndex(methodHeader);
					methodHeader = code.substring(mSindex, i+1);
					sb.append(methodHeader);

					firstFun = false;
				}
				stack.push(c);

			} else if('}' == c) {
				stack.pop();
				if(stack.empty()) {
					methodStrs.add(sb.toString());
					sb.delete(0, sb.length());
				}
			}
		}


		for (String cName: methodStrs) {

			Pattern fp = Pattern.compile("(public|private|protected)[\\s\\S]*?\\([\\s\\S]*?\\)");
			Matcher mp = fp.matcher(cName);

			if( !mp.find()) {

				// check class
				// private class TreeId {
				Pattern checkClassP = Pattern.compile("(private|public)?.*(class|enum).*\\{");
				Matcher ccMp = checkClassP.matcher(cName);
				if(ccMp.find()) {
					boolean isInnerClass = cName.substring(0, cName.indexOf("{")).indexOf("class") > -1;
					if(isInnerClass) {
						topLevelClass.addInnerClass(InnerClassUtil.formatInnerClass(cName));
					} else {
						topLevelClass.addInnerEnum(InnerEnumUtil.formatInnerEnum(cName));
					}

				} else {

					// check static block code
					boolean isStatic = cName.trim().indexOf("static") > -1;
					InitializationBlock initializationBlock = new InitializationBlock();
					initializationBlock.setStatic(isStatic);

					String blockStr = cName.substring(cName.indexOf("{")+1, cName.lastIndexOf("}")).trim();
					initializationBlock.addBodyLines(Arrays.asList(blockStr.split(_n)));
					topLevelClass.addInitializationBlock(initializationBlock);

					String staticBlocks;
					if(isStatic) {
						staticBlocks = cName.substring(0, cName.indexOf("static"));
					} else {
						staticBlocks = cName.substring(0, cName.indexOf("{"));
					}

					if(StringUtils.isNotBlank(staticBlocks)) {
						for(String ll: staticBlocks.split(_n)) {
							initializationBlock.addJavaDocLine(ll.trim());
						}
					}
				}
				continue;
			}
			String fun = mp.group().trim();

			int s = fun.indexOf("private") > -1 ? fun.indexOf("private") :(
					fun.indexOf("public") > -1 ? fun.indexOf("public") :(
							fun.indexOf("public") > -1 ? fun.indexOf("protected") : 0 )
			);

			String vis = fun.substring(s, fun.indexOf(" "));
			fun = fun.substring(fun.indexOf(" "), fun.length()).trim();
			String rType = fun.substring(0, fun.indexOf(" "));
			String fName = fun.substring(rType.length(), fun.indexOf("(")).trim();
			String params = fun.substring(fun.indexOf("(")+1, fun.indexOf(")")).trim();

//			System.out.println(vis + "|"+rType + "| " + fName + "(" + params + ")");

			MyMethod method = new MyMethod(fName);
			method.setVisibility(JavaVisibility.valueOf(vis.toUpperCase()));
			method.setReturnType(new FullyQualifiedJavaType(rType));

			// throws
//			method.addException();
			String exceptions = cName.substring(cName.indexOf(")")+1, cName.indexOf("{")).trim();
			if(StringUtils.isNotBlank(exceptions)) {
				exceptions = exceptions.substring(exceptions.indexOf(" ")+1, exceptions.length());
				String[] exs = exceptions.split(",");
				for(String e: exs) {
					method.addException(new FullyQualifiedJavaType(e));
				}
			}

			// add method lines ： 前后有可能会多出空回车
			String mLines = cName.substring(cName.indexOf("{")+1, cName.lastIndexOf("}"));
			List<String> bodyLines = new ArrayList<>();
			for (String line : Arrays.asList(mLines.trim().split(_n))) {
				String lineTmp = line.trim();
				if(StringUtils.isNotBlank(lineTmp)) {
					bodyLines.add(lineTmp);
				}
			}

			method.addBodyLines(bodyLines);

			if(methods != null) {
				methods.add(method);
			}
			topLevelClass.addMethod(method);

			// method parameter
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

			if(cName.lastIndexOf("/*") > -1) {
				String javaDocLine = cName.substring(cName.lastIndexOf("/*"), cName.lastIndexOf("*/")+2);
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
		
		String code = IOUtils.toString(new FileInputStream("/Users/lwl/Documents/source-code/java-code/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/service/impl/ProjectServiceImpl.java"), "utf-8");
//		String code = IOUtils.toString(new FileInputStream("/Users/leiwuluan/Documents/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/service/impl/ProjectServiceImpl.java"), "utf-8");

		TopLevelClass t = new TopLevelClass("");
		getInterfaceImplMethodsAndSetClassProperties(t,null, code);


		System.out.println(t.getFormattedContent());
	}

}
