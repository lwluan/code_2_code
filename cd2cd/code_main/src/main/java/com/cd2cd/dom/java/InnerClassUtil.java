package com.cd2cd.dom.java;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InnerClassUtil {

    private static Logger log = LoggerFactory.getLogger(InnerClassUtil.class);

    public static String getClassHeader(String code) {
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.*class.+\\{");
        Matcher m = p.matcher(code);
        if(m.find()) {
            return m.group();
        }
        return null;
    }

    public static String getInnerClassName(String classH) {

        /**
         * TODO 有问题
         * public static class dddd<TT extends SysUser & SysUserService, E, F>extends BaseRes<SysUser>
         * public static class dddd<T> {}
         * public static class dddd {}
         * public static class dddd<T extends SysUser & SysUserService> extends SysUser implements Serializable { }
         */

        String ss = classH.split("class")[1];
        int leftInt = ss.indexOf("<");
        int extendsInt = ss.indexOf("extends");
        int impleInt = ss.indexOf("implements");
        int closeInt = ss.indexOf("{");

        int lastInt = leftInt > -1 && leftInt < extendsInt ? leftInt :
                (extendsInt>-1 ? extendsInt :
                        (impleInt > -1 ? impleInt : closeInt));

        ss = ss.substring(0, lastInt);
        ss = ss.trim();

        return ss;
    }



    public static String getVisible(String classH) {
        if(classH.indexOf("private") > -1) {
            return "private";
        } else if(classH.indexOf("public") > -1) {
            return "public";
        } else if(classH.indexOf("protected") > -1) {
            return "protected";
        }
        return "DEFAULT";
    }

    public static void setClassFiled(InnerClass innerClass, String code) {
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.+;");
        Matcher m = p.matcher(code);
        if(m.find()) {
            Field field = new Field();

            String fieldStr = m.group();

            String vis = getVisible(fieldStr);
            field.setVisibility(JavaVisibility.valueOf(vis.toUpperCase()));

            boolean isStatic = fieldStr.indexOf(" static ") > -1;
            boolean isFinal = fieldStr.indexOf(" final ") > -1;
            boolean isTransient = fieldStr.indexOf(" transient ") > -1;
            boolean isVolatile = fieldStr.indexOf(" volatile ") > -1;

            field.setStatic(isStatic);
            field.setFinal(isFinal);
            field.setTransient(isTransient);
            field.setVolatile(isVolatile);

            String[] ss = fieldStr.split("=");
            if(ss.length > 1) {
                String initializationString = ss[1];
                initializationString = initializationString.substring(0, initializationString.lastIndexOf(";"));
                initializationString = initializationString.trim();
                field.setInitializationString(initializationString);
            }
            innerClass.addField(field);

        }
    }

    public static void setClassComment(InnerClass innerClass, String code) {
        // TODO 类注释
    }

    public static void setClassMethodAndProperties(InnerClass innerClass, String code) {
        Map<String, MyMethod> mapDic = new HashMap<>();
        List<MyMethod> methods = new ArrayList<>();
        innerClass.getMethods().addAll(methods);

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

                    int mSindex = CodeUtils.lastEndIndex(methodHeader);
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

                Pattern checkClassP = Pattern.compile("(private|public)?.*(class|enum).*\\{");
                Matcher ccMp = checkClassP.matcher(cName);
                if(ccMp.find()) {
                    boolean isInnerClass = cName.substring(0, cName.indexOf("{")).indexOf("class") > -1;
                    if(isInnerClass) {
                        innerClass.addInnerClass(InnerClassUtil.formatInnerClass(cName));
                    } else {
                         innerClass.addInnerEnum(InnerEnumUtil.formatInnerEnum(cName));
                    }

                } else {

                    // check static block code
                    boolean isStatic = cName.trim().indexOf("static") > -1;
                    InitializationBlock initializationBlock = new InitializationBlock();
                    initializationBlock.setStatic(isStatic);

                    String blockStr = cName.substring(cName.indexOf("{")+1, cName.lastIndexOf("}")).trim();
                    initializationBlock.addBodyLines(Arrays.asList(blockStr.split(CodeUtils._n)));
                    innerClass.addInitializationBlock(initializationBlock);

                    String staticBlocks;
                    if(isStatic) {
                        staticBlocks = cName.substring(0, cName.indexOf("static"));
                    } else {
                        staticBlocks = cName.substring(0, cName.indexOf("{"));
                    }

                    if(StringUtils.isNotBlank(staticBlocks)) {
                        for(String ll: staticBlocks.split(CodeUtils._n)) {
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

            log.info("fName={}", fName);

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
            List<String> bodyLines = Arrays.asList(mLines.split(CodeUtils._n));
            method.addBodyLines(bodyLines);

            if(methods != null) {
                methods.add(method);
            }

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
                int si = cName.indexOf(CodeUtils._n);
                int genI = cName.indexOf(genStr);
                if(si < genI) {
                    String comment = cName.substring(si+CodeUtils._n.length(), genI);
                    if(comment.lastIndexOf(CodeUtils._n) > -1) {
                        comment = comment.substring(0, comment.lastIndexOf(CodeUtils._n));
                        String[] lines = comment.split(CodeUtils._n);

                        for(String l: lines) {
                            method.getCustomComment().add(" " + l.trim());
                        }
                    }
                }
            }
        }

        innerClass.getMethods().addAll(methods);
    }

    public static void setSuperClass(InnerClass innerClass, String classH) {

        /**
         * static class dddd extends fff {}
         * static class dddd<T> extends fff {}
         * static class dddd extends fff<EE> {}
         * public static class dddd<SysUserService>
         * public static class dddd<T extends SysUser & SysUserService> extends BaseRes<SysUser> implements Serializable { }
         *
         * #**1; extends SysUser & SysUserService>
         * #**2; extends BaseRes<SysUser> implements Serializable { }
         */
        String superC = null;
        String[] ss = classH.split("extends");
        int ssLen = ss.length;
        if(ssLen == 3) { // xxxx extends xxxxx extends bbbb
            superC = ss[2].trim();
            int lastInt = (superC.indexOf("implements") > -1 ? superC.indexOf("implements")
                    :(superC.indexOf("{")));
            superC = superC.substring(0, lastInt);
        } else if(ssLen == 2) {
            superC = ss[1].trim();
            if(superC.indexOf("<") > -1) { // #**2;
                int lastInt = superC.indexOf("implements") > -1 ? superC.indexOf("implements")
                        :superC.indexOf("{");
                superC = superC.substring(0, lastInt);

                // TODO className = classH.substring(classH.indexOf("class")+5, classH.indexOf(">"+1));
            } else {

            }
        }

        // 类范型
        setClassTypes(classH, innerClass);

        if(StringUtils.isNotBlank(superC)) {
            innerClass.setSuperClass(superC);
        }

        // implements class
        String ii = "implements";
        if(classH.indexOf(ii) > -1) {

            String interStr = classH.substring(classH.indexOf(ii) + ii.length(), classH.indexOf("{")).trim();
            String[] isArr = interStr.split(",");
            for(String is: isArr) {
                innerClass.addSuperInterface(new FullyQualifiedJavaType(is.trim()));
            }


        }
    }

    /**
     * 设置类型类型
     * @param classH
     * @param innerClass
     */
    private static void setClassTypes(String classH, InnerClass innerClass) {
        String typeS = classH.split("class")[1];
        int leftInt = typeS.indexOf("<");
        int extendsInt = typeS.indexOf("extends");

        String typeStrs = null;
        if(leftInt > -1) {
            if(extendsInt > -1) {
                if(leftInt < extendsInt) {
                    typeStrs = typeS.substring(leftInt+1, typeS.indexOf(">"));
                }
            } else {
                typeStrs = typeS.substring(leftInt+1, typeS.indexOf(">"));
            }
        }
        if(StringUtils.isNotBlank(typeStrs)) {
            typeStrs = typeStrs.trim();

            String[] sss = typeStrs.split(",");
            for(String types: sss) {
                String[] ssArr = types.split("extends");
                String name = ssArr[0];
                TypeParameter tp = new TypeParameter(name.trim());

                if(ssArr.length > 1) {
                    String extendStrs = ssArr[1].trim();
                    String[] essArr = extendStrs.split("&");
                    for(String eas: essArr) {
                        tp.getExtendsTypes().add(new FullyQualifiedJavaType(eas.trim()));
                    }
                }
                innerClass.getTypeParameters().add(tp);
            }
        }

    }

    public static InnerClass formatInnerClass(String code) {
        code = code.trim();

        String classH = getClassHeader(code);

        // class name
        String className = getInnerClassName(classH);
        InnerClass innerClass = new InnerClass(className);

        // set superClass/superInterfaceTypes/typeParameters
        setSuperClass(innerClass, classH);

        // class comment
        setClassComment(innerClass, code);

        // class visible
        String vis = getVisible(classH);
        innerClass.setVisibility(JavaVisibility.valueOf(vis.toUpperCase()));
        innerClass.setStatic(classH.indexOf(" static ") > -1);
        innerClass.setFinal(classH.indexOf(" final ") > -1);
        innerClass.setAbstract(classH.indexOf(" abstract ") > -1);

        // class field
        setClassFiled(innerClass, code);

        // class method/initializationBlocks/innerClass/innerEnums
        setClassMethodAndProperties(innerClass, code);

        return innerClass;
    }

    public static void main(String[] args) throws IOException {
        String s = "public static class dddd<T extends SysUser & SysUserService, E, F> extends BaseRes<SysUser> implements Serializable { }";
        s = "public static class rewr extends BaseRes<SysUser> implements Serializable,Serializable1, 3 { }";


        s = IOUtils.toString(new FileInputStream(new File("/Users/leiwuluan/Documents/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/service/impl/ProjectServiceImpl.java")), "utf-8");
//        s = IOUtils.toString(new FileInputStream(new File("/Users/lwl/Documents/source-code/java-code/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/service/impl/ProjectServiceImpl.java")), "utf-8");

        InnerClass in = formatInnerClass(s);

//        System.out.println(in.getFormattedContent(0, new TopLevelClass("")));

    }
}
