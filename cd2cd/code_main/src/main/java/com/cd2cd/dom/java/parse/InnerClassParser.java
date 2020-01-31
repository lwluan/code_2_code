package com.cd2cd.dom.java.parse;

import com.cd2cd.dom.java.InnerClassUtil;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InnerClassParser {

    public static void parseInnerClass(InnerClass innerClass, String code) {

        String classH = getClassHeader(code);

        // visible
        String visible = getVisible(classH);
        innerClass.setVisibility(JavaVisibility.valueOf(visible.toUpperCase()));

        // 类注释+注解
        setClassCommentAndAnnotation(innerClass, code, classH);

        // 类继承-父类、范型、接口
        setSuperClass(innerClass, classH);

        // 过滤内部类、枚举、方法、静态块、块、成员变量列表： 并递归生成内部类、枚举
        setInnerComponent(innerClass, code, classH);

    }

    public static void setInnerComponent(InnerClass innerClass, String code, String classH) {

        /**
         * 过滤类每个组成部份：再每个部份进件处理；
         * block; static block; method; class
         */

        // class body
        String block = code.substring(code.indexOf(classH)+classH.length(), code.lastIndexOf("}"));

        List<String> blocks = getBlocksFromCode(block);


        List<String> fieldsList = new ArrayList<>();
        List<String> methodsList = new ArrayList<>();
        List<String> initBlockList = new ArrayList<>();
        List<String> innerClassList = new ArrayList<>();
        List<String> innerEnumsList = new ArrayList<>();

        for(String bb: blocks) {
//            System.out.println(bb +"***************-----------++++++++++++--------+++++++------line..-___=====\n\n");
            String[] lines = bb.trim().split("\n");

            boolean commOpen = false;
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<lines.length; i++) {

                String line = lines[i];
                sb.append(line+"\n");

                // 存在注释块中的代码为无效
                /**
                 * 例：
                 * continue;
                 */

                int commSIndex = line.indexOf("/*");
                int commEIndex = line.lastIndexOf("*/");

                // 情况：/**{ | /**}
                if(commSIndex > -1 && line.trim().indexOf("/*") == 0) {
                    commOpen = true;
                }

                // 情况：{**/  | }**/
                if(commEIndex>-1) {
                    commOpen = false;
                }

                if(commOpen || line.trim().indexOf("//") == 0) {
                    continue;
                }

                LineType lineType = checkLineType(line);

                // open 时，判断是开始位置还是在代码后面例：1(enum aaa{ /** /**); 2(/** enum bbb{} **/)

                if(lineType == LineType.Other) {

                } else {
                    if (lineType == LineType.Fields) {
                        fieldsList.add(sb.toString());
                        sb.delete(0, sb.length());
                    } else {

                        for(int j=i+1; j<lines.length; j++) {
                            sb.append(lines[j]+"\n");
                        }
                        // 后面全部代码一起加入跳出
                        // InitializationBlock,InnerClasses,InnerEnums,Methods

                        switch (lineType) {
                            case Methods:
                                methodsList.add(sb.toString());
                                break;
                            case InitializationBlock:
                                initBlockList.add(sb.toString());
                                break;
                            case InnerClasses:
                                innerClassList.add(sb.toString());
                                break;
                            case InnerEnums:
                                innerEnumsList.add(sb.toString());
                                break;
                        }
                        break;
                    }
                }


            }
        }


        // 过滤出方法
        handleMethod(methodsList, innerClass);

        // 过滤出inner内部类, interface
        handleInnerClass(innerClassList, innerClass);

        // inner interface

        // inner enum

        // 过滤出 static block

        // 过滤出 init block

        // 成员变量 fields
        handleFields(fieldsList, innerClass);

        // 设置成员变量 + 注解 + @Value("${aa.ba.val}")

        // setClassField(innerClass, code);
    }

    /**
     * 内部类或内部接口
     * @param innerClassList
     * @param innerClass
     */
    private static void handleInnerClass(List<String> innerClassList, InnerClass innerClass) {

        for(String block: innerClassList) {

            System.out.println("block=" + block);

            String classH = getClassHeader(block);
            if(classH.indexOf("interface") > -1) {
                // interface
            } else {
                // class

                // interface

                // class abs

                System.out.println("classH="+classH);
                String className = InnerClassUtil.getInnerClassName(classH);

                InnerClass childClass = new InnerClass(className);

//            Interface
                innerClass.addInnerClass(childClass);
                System.out.println("block---" + block);
            }

        }

    }

    private static void handleFields(List<String> fieldsList, InnerClass innerClass) {
        for(String block: fieldsList) {

            String[] lines = block.split("\n");
            Field field = new Field();
            innerClass.addField(field);

            boolean commOpen = false;
            for(int i=0; i<lines.length; i++) {
                String line = lines[i];
                if (line.trim().indexOf("/*") == 0) {
                    commOpen = true;
                }

                if (commOpen) {
                    field.addJavaDocLine(line.trim());
                }
                if (line.trim().indexOf("*/") > -1) {
                    commOpen = false;
                }

                // 注解 Annotation
                if (line.trim().indexOf("@") == 0) {
                    field.addAnnotation(line.trim());
                } else if (i==lines.length - 1) {
                    // 成员变量

                    JavaVisibility visibility = JavaVisibility.valueOf(getVisible(line).toUpperCase());
                    if(visibility != null) {
                        field.setVisibility(visibility);
                    }

                    // static
                    if(line.indexOf("static") > -1) {
                        field.setStatic(true);
                    }

                    // final
                    if(line.indexOf("final") > -1) {
                        field.setFinal(true);
                    }

                    // name
                    String nn = line.substring(0, line.indexOf(";"));
                    String name = nn.substring(nn.lastIndexOf(" ")).trim();
                    field.setName(name);

                    // type
                    String tt = line.substring(0, line.lastIndexOf(name));
                    tt = tt.replaceAll("private ",  "");
                    tt = tt.replaceAll(" final ",  "");
                    tt = tt.replaceAll(" static ",  "");
                    tt = tt.replaceAll(" public ",  "");
                    tt = tt.replaceAll(" protected ",  "");
                    field.setType(new FullyQualifiedJavaType(tt));
                }
            }

        }
    }


    private static LineType checkLineType(String line) {

        LineType lineType = LineType.Other;


        // public static void cccc(@RequestBody @NotNull @Validated({AddValid.class, UpdateValid.class}) String ddd) {
        // 过滤掉以上

        if(line.indexOf("@") == -1 && isMatch(line, "class.+\\{") || isMatch(line, "interface.+\\{")) {
            // 抽象
            lineType =  LineType.InnerClasses;

        }else if(isMatch(line, "enum.+\\{")) {
            // 枚举
            lineType =  LineType.InnerEnums;

        }else if(isMatch(line, ".+\\(.*\\).*\\{")) {
            // 方法
            lineType =  LineType.Methods;

        }else if(line.trim().indexOf("static") == 0 || line.trim().indexOf("{") == 0) {
            // 静态块代码
            lineType =  LineType.InitializationBlock;

        }else if(isMatch(line, ".*\\s+.+;")) {
            // 成员变量
            lineType =  LineType.Fields;
        }

        System.out.println(line+"     ------     "+lineType);

        return lineType;
    }

    private static boolean isMatch(String line, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(line);
        return m.find();
    }

    // 过滤出类中的所有方法
    private static void handleMethod(List<String> list, InnerClass innerClass) {

        for(String block: list) {

            Method method = new Method();
            innerClass.addMethod(method);


            // 方法注释； 注解；
            String[] lines = block.split("\n");
            boolean commOpen = false;
            for(int i=0; i<lines.length; i++) {
                String line = lines[i];
                if(line.trim().indexOf("/*") == 0) {
                    commOpen = true;
                }

                if(commOpen) {
                    method.addJavaDocLine(line.trim());
                }
                if(line.trim().indexOf("*/") >-1 ) {
                    commOpen = false;
                }

                // 注解 Annotation
                if(line.trim().indexOf("@") == 0) {
                    method.addAnnotation(line.trim());
                } else if(isMatch(line, ".+\\(.*\\).*\\{")) {
                    // 方法头

                    String funStr = line.trim();

                    // 方法名
                    int funLInt = funStr.indexOf("(");
                    String s = funStr.substring(0, funLInt);
                    String name = s.substring(s.lastIndexOf(" ")).trim();
                    method.setName(name);

                    // 是否为构造函数
                    if(innerClass.getType().getShortName().equals(name)) {
                        method.setConstructor(true);
                    }

                    JavaVisibility visibility = JavaVisibility.valueOf(getVisible(funStr).toUpperCase());
                    if(visibility != null) {
                        method.setVisibility(visibility);
                    }

                    // static
                    if(funStr.indexOf("static") > -1) {
                        method.setStatic(true);
                    }

                    // final
                    if(funStr.indexOf("final") > -1) {
                        method.setFinal(true);
                    }


                    // 返回值
                    String reType = funStr.substring(funStr.indexOf(" "), funStr.indexOf(name)).trim();
                    reType = reType.replaceAll("static", "");
                    reType = reType.replaceAll("final", "");
                    method.setReturnType(new FullyQualifiedJavaType(reType));


                    // 方法参数
                    String params = funStr.substring(funLInt+1, funStr.lastIndexOf(")")).trim();
                    if(StringUtils.isNotBlank(params)) {
                        String[] pp = params.split(",");
                        for(String p: pp) {

                            // cccc(@RequestBody @NotNull @Validated({AddValid.class, UpdateValid.class}) String ddd)

                            String nn = p.substring(p.lastIndexOf(" ")).trim();
                            String tt = p.substring(0, p.lastIndexOf(" ")).trim();

                            Parameter parameter = new Parameter(new FullyQualifiedJavaType(tt), nn);
                            method.addParameter(parameter);
                        }
                    }
                    System.out.println("params=" + params);

                    // throws
                    String k = "throws";
                    if(funStr.indexOf(k) > -1) {
                        int ii = funStr.indexOf(k) + k.length();
                        String exceptions = funStr.substring(ii, funStr.indexOf("{", ii+1)).trim();
                        System.out.println("exceptions=" + exceptions);

                        String[] exs = exceptions.split(",");
                        for (String e : exs) {
                            method.addException(new FullyQualifiedJavaType(e.trim()));
                        }
                    }

                    // 方法体
                    for(int j=i+1; j<lines.length; j++) {

                        String ll = lines[j].trim();
                        // last } 不加入
                        if(j==lines.length-1) {

                            // 去除注释中的内容
                            if(ll.indexOf("//") > -1) {
                                ll = ll.substring(0, ll.indexOf("//"));
                            }

                            ll = ll.substring(0, ll.lastIndexOf("}"));
                        }
                        if(StringUtils.isNotBlank(ll)) {
                            method.addBodyLine(ll.trim());
                        }
                    }

                    if(funStr.indexOf("abstract") == -1 && CollectionUtils.isEmpty(method.getBodyLines())) {
                        method.addBodyLine("");
                    }

                    break;
                }
            }
        }
    }

    private static int doIndex(String line, char sChar) {
        int ii = line.indexOf(sChar);
        if(ii > -1) {
            if(line.charAt(ii-1) == '(') {
                return line.indexOf(sChar, ii+1);
            } else {
                return ii;
            }
        }
        return -1;
    }

    private static int doLastIndex(String line, char eChar) {
        int ii = line.indexOf(eChar);
        if(ii > -1) {
            if(line.length() > ii+1 && line.charAt(ii+1) == ')') {
                return line.indexOf(eChar, ii+1);
            } else {
                return ii;
            }
        }
        return -1;
    }

    private static List<String> getBlocksFromCode(String block) {
        List<String> blocks = new ArrayList<>();

        boolean debug = false;

        // fetch {} block
        char sChar = '{';
        char eChar = '}';
        char aChar = '@'; // 注解
        char strChar = '"'; // 双引号

        Stack<Character> stack = new Stack<>();
        StringBuilder bStr = new StringBuilder();
        String[] lines = block.split("\n");

        boolean commOpen = false;
        for(String line: lines) {

            bStr.append(line+"\n");

//            int sCharIndex = line.indexOf(sChar);
//            int eCharIndex = line.indexOf(eChar);
            int sCharIndex = doIndex(line, sChar);
            int eCharIndex = doLastIndex(line, eChar);

            int aCharIndex = line.indexOf(aChar);
            int strCharIndex = line.indexOf(strChar);
            int commSIndex = line.indexOf("/*");
            int commEIndex = line.lastIndexOf("*/");
            int commLIndex = line.indexOf("//");


            boolean hasSChar = sCharIndex > -1 && !commOpen;
            boolean hasEChar = eCharIndex > -1 && !commOpen;

            // 未在注释中
            if(!commOpen) {

                // 注解中是否有 { }  *******
                if (aCharIndex > -1) {
                    hasEChar = false;

                    // {}  {: {大于}
                    if( ! (line.lastIndexOf("{") > line.lastIndexOf("}")) ) {
                        hasSChar = false;
                    }
                }

                // 字符【串】中是否有 "{" "}"
                if(strCharIndex > -1) {
                    int hasNum = 0;
                    if (hasSChar) {
                        hasNum = 0;
                        for (int i = 0; i < sCharIndex; i++) {
                            if (line.charAt(i) == strChar) {
                                hasNum++;
                            }
                        }
                        if (hasNum % 2 != 0) {
                            hasSChar = false;
                        }
                    }
                    if (hasEChar) {
                        hasNum = 0;
                        for (int i = 0; i < eCharIndex; i++) {
                            if (line.charAt(i) == strChar) {
                                hasNum++;
                            }
                        }
                        if (hasNum % 2 != 0) {
                            hasEChar = false;
                        }
                    }
                }

                // 字符中是否有 '{' '}'
                if(line.indexOf("'{'") +1 == sCharIndex) {
                    hasSChar = false;
                }

                if(line.indexOf("'}'") + 1 == eCharIndex) {
                    hasEChar = false;
                }
            }
            /**
             * 检查标记是否有效:
             * 1、是否在 （/** * /）内部
             * 2、是否在 // 行上
             * 3、@Null(groups={A.class}) @Value("${aa.cc.value}")
             * 4、String a = "{{";
             * 5、char aa = '{';
             * 6、一行多个分割 if(true){ } else {  单个字符去判断
             */

            //  // { }
            if(commLIndex > -1) {
                if(sCharIndex > commLIndex) {
                    hasSChar = false;
                }

                if(eCharIndex > commLIndex) {
                    hasEChar = false;
                }
            }

            // 情况：/**{ | /**}
            if(commSIndex > -1) {
                commOpen = true;

                if(sCharIndex > commSIndex) {
                    hasSChar = false;
                }

                if(eCharIndex > commSIndex) {
                    hasEChar = false;
                }
            }

            // 情况：{**/  | }**/
            if(commEIndex>-1) {
                commOpen = false;
                if(sCharIndex > -1 && sCharIndex < commEIndex) {
                    hasSChar = false;
                }

                if(eCharIndex > -1 && eCharIndex < commEIndex) {
                    hasEChar = false;
                }
            }

            // 在注释内部，忽略
            // } // test { }
            if(debug) {
                System.out.println("line=" + line + "\n____________________\n commOpen="
                        + commOpen + "，hasSChar=" + hasSChar + "，hasEChar=" + hasEChar
                +",sCharIndex=" + sCharIndex + ",eCharIndex=" + eCharIndex+"\n\n");
            }
            if(commOpen) {
                if( !hasSChar)
                {
                    continue;
                }
            }

            if(hasSChar) {
                if(debug) {
                    System.out.println("push ++++++++++++++++++++++++++++++++++++");
                }
                stack.push(sChar);
            }

            if(hasEChar) {

                // 少了 pop 一行多个分割 if(true){ } else {  单个字符去判断
                popOrAddStack(stack, line);

                String bb = bStr.toString();
                if(debug) {
                    System.out.println("pop -------------------");
                }
                stack.pop();

                if(stack.isEmpty()) {
                    if(debug) {
                        System.out.println("*********************************************");
                    }
                    // end block
                    blocks.add(bb);
                    bStr.delete(0, bStr.length());
                }
            }

        }
        return blocks;
    }

    // 少了 pop 一行多个分割 if(true){ } else {  单个字符去判断
    // 以上程序只处理了一种情况 {}
    private static void popOrAddStack(Stack<Character> stack, String line) {
        // /** */;  //
        // {
        // }

        // @Abbbb(vaava={cccaa.class,kjbkda.class}); 不计算

        char sChar = '{';
        char eChar = '}';
        int commSIndex = line.indexOf("/*");
        int commEIndex = line.lastIndexOf("*/");


        // 去除括号里的 标记
        String s = line;
        s = s.replaceAll("\"/\\*", "");
        s = s.replaceAll("\"//", "");

        // 去除 /** /*
        if(commSIndex > -1) {
            int eInt = commEIndex>-1 ? commEIndex : s.length();
            String ss = s.substring(eInt-1);
            s = s.substring(0, commSIndex) + ss;
        }

        // 去除 s="{"; s='{'; s="}"; s='}'; 在字符串中的不计数
        s = s.replaceAll("\'\\{\'", "");
        s = s.replaceAll("\'\\}\'", "");

        int commLIndex = s.indexOf("//");
        // 去除  // 后面代码
        if(commLIndex > -1) {
            s = s.substring(0, commLIndex);
        }

        char strChar = '"';// 去除 字符串里面的内容。
        int strint = s.indexOf(strChar);
        while(strint > -1) {
            int str2int = s.indexOf(strChar, strint+1);
            if(str2int > -1) {
                String ii = s.substring(str2int+1);
                s = s.substring(0, strint) + ii;
            }
            strint = s.indexOf(strChar, str2int+1);
        }

        // {   " 44{33 " {  '{'  // "{
//        System.out.println(s);

        int sint = 0;
        int sCount = 0;
        while(sint > -1) {
            sint = s.indexOf(sChar, sint+1);
//            System.out.println("sint="+sint);
            sCount++;
        }

        int eint = 0;
        int eCount = 0;
        while(eint > -1) {
            eint = s.indexOf(eChar, eint+1);
//            System.out.println("eint="+eint);
            eCount++;
        }

        if(sCount >1 && eCount<sCount) {
            stack.push('{');
        }
//        System.out.println("sCount="+sCount+", eCount=" + eCount+", eint="+eint+",sint="+sint);

    }

    public static void setClassField(InnerClass innerClass, String code) {
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.+;");
        Matcher m = p.matcher(code);
        while(m.find()) {
            Field field = new Field();

            String fieldStr = m.group();

            System.out.println("*************fieldStr=" + fieldStr);

            String vis = InnerClassParser.getVisible(fieldStr);
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

    /**
     * 设置类注释
     * @param innerClass
     * @param code
     */
    private static void setClassCommentAndAnnotation(InnerClass innerClass, String code, String classH) {

        String classTop = code.substring(0, code.indexOf(classH));
        String[] lines = classTop.split("\n");
        List<String> comments = new ArrayList<>();
        List<String> annotions = new ArrayList<>();
        for(int i=lines.length-1; i>0; i--) {

            String line = lines[i].trim();
            if(line.startsWith("import") || line.startsWith("}")) {
                break;
            }
            if(line.startsWith("@")) {
                annotions.add(line);
            } else {
                comments.add(0, line);
            }
        }

        for(String s: comments) {
            innerClass.getJavaDocLines().add(s);
        }

        for(String ss: annotions) {
            innerClass.getAnnotations().add(ss);
        }
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

    public static String getClassHeader(String code) {
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.*(class|interface).+\\{");
        Matcher m = p.matcher(code);
        while(m.find()) {
            String line = m.group();
            if( ! line.startsWith("//")) {
                return line;
            }
        }
        return null;
    }

    public static void setSuperClass(InnerClass innerClass, String classH) {

        /**
         * static class dddd extends fff {}
         * static class dddd<T> extends fff {}
         * static class dddd extends fff<EE> {}
         * public static class dddd<SysUserService>
         * public class DemoClass<T extends TestInterface2, E extends TestInterface2>
         * public static class dddd<T extends SysUser & SysUserService> extends BaseRes<SysUser> implements Serializable { }
         * public static class dddd<T extends SysUser & SysUserService> extends BaseRes<SysUser> implements A<B> { }
         *
         * #**1; extends SysUser & SysUserService>
         * #**2; extends BaseRes<SysUser> implements Serializable { }
         */

        String className = innerClass.getType().getShortName();

        String ii = "implements";
        String ext = "extends";
        String implStr = null;
        String superStr = null;
        String classTypeStr = null;

        String firstStr = null;
        // 类有接口
        if(classH.indexOf(ii) > 0) {
            String[] ss = classH.split(ii);

            // 类接口部份
            implStr = ss[1];
            implStr = implStr.substring(0, implStr.lastIndexOf("{")).trim();

            // 类范型 + 类父类继承
            firstStr = ss[0];

        } else {
            firstStr = classH.substring(0, implStr.lastIndexOf("{")).trim();
        }

        // 获取【类范型】 <ClassA> | <T extends ClassA> | <T extends ClassA, E extends ClassB>
        firstStr = firstStr.substring(firstStr.indexOf(className) + className.length());
        if(firstStr.indexOf("<") < 5 && firstStr.indexOf("<") > -1) {
            classTypeStr = firstStr.substring(firstStr.indexOf("<")+1, firstStr.indexOf(">"));
        }
        if(StringUtils.isNotBlank(classTypeStr)) {
            firstStr = firstStr.substring(firstStr.indexOf(classTypeStr)+classTypeStr.length());
        }


        // 类【父类】继承 extends : ClassA | ClassA<ClassB>
        if(firstStr.indexOf(ext) > -1) {
            superStr = firstStr.substring(firstStr.indexOf(ext) + ext.length()).trim();
        }

        // 类【范型】
        setClassTypes(classTypeStr, innerClass);

        // 类【父类】
        if(StringUtils.isNotBlank(superStr)) {
            innerClass.setSuperClass(superStr);
        }

        // 类【接口】
        if(StringUtils.isNotBlank(implStr)) {
            String[] isArr = implStr.split(",");
            for(String is: isArr) {
                innerClass.addSuperInterface(new FullyQualifiedJavaType(is.trim()));
            }
        }
    }

    /**
     * 设置类型类型
     * @param innerClass
     */
    private static void setClassTypes(String classTypeStr, InnerClass innerClass) {
        String typeStrs = classTypeStr;
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

    private static void testClass() throws IOException {
        String url = "/Users/lwl/Documents/source-code/java-code/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java";
//        url = "/Volumes/data/code-sources/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java";
        File file = new File(url);
        TopClassParser cf = new TopClassParser(file);

        TopLevelClass cc = cf.toTopLevelClass();
        System.out.println(cc.getFormattedContent());
    }

    public static void main(String[] args) throws IOException {

        testClass();

//        String s = " /** if(true){} **/ s='{'; String aa=\"{\"; if(true){ }else{ } { // {";
//        popOrAddStack(null, s);
        String line = "private final static Logger LOG = LoggerFactory.getLogger(DemoClass.class); // iiirewjr";
//        line = "String name;";
        line = "abstract class TestAbstract {";
        line = "private abstract class TestAbstract {";
        line = "public static void main(String[] args) {";
        line = "public static void main(){";
        line = "public void main(){ // 99sfd";
//        line = "static {";
//        line = "iiii{ // ";

        Pattern p = Pattern.compile("(static|\\s)*.*\\{");
        Matcher m = p.matcher(line);
        System.out.println(m.find()+"------");


    }
}
