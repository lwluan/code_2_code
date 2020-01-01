package com.cd2cd.dom.java.parse;

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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


        List<String> methodList = new ArrayList<>();
        for(String bb: blocks) {
            System.out.println(bb +"***************\n\n");


        }

        // 过滤出方法

        // 过滤出inner内部类

        // inner interface

        // inner enum

        // 过滤出 static block

        // 过滤出 init block

        // 成员变量


        // 设置成员变量 + 注解 + @Value("${aa.ba.val}")
//        setClassField(innerClass, code);

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

            int sCharIndex = line.indexOf(sChar);
            int eCharIndex = line.indexOf(eChar);
            int aCharIndex = line.indexOf(aChar);
            int strCharIndex = line.indexOf(strChar);
            int commSIndex = line.indexOf("/*");
            int commEIndex = line.indexOf("*/");
            int commLIndex = line.indexOf("//");


            boolean hasSChar = sCharIndex > -1 && !commOpen;
            boolean hasEChar = eCharIndex > -1 && !commOpen;

            // 未在注释中
            if(!commOpen) {

                // 注解中是否有 { }
                if (aCharIndex > -1) {
                    hasSChar = false;
                    hasEChar = false;
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
                System.out.println("line=" + line + "\t\t\t\t **** commOpen=" + commOpen + "，hasSChar=" + hasSChar + "，hasEChar=" + hasEChar);
            }
            if(commOpen) {
                if( !hasSChar)
                {
                    continue;
                }
            }

            if(hasSChar) {
                if(debug) {
                    System.out.println("push ++++++++++++++++++");
                }
                stack.push(sChar);
            }



            if(hasEChar) {

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
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.*class.+\\{");
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
        if(firstStr.indexOf("<") < 5) {
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
        File file = new File("/Volumes/data/code-sources/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java");
        TopClassParser cf = new TopClassParser(file);

        TopLevelClass cc = cf.toTopLevelClass();
//        System.out.println(cc.getFormattedContent());
    }

    public static void main(String[] args) throws IOException {

        testClass();

        String s = "/* * {name} **/ ";
        System.out.println(s.indexOf("/*"));
        System.out.println(s.indexOf("*/"));



    }
}
