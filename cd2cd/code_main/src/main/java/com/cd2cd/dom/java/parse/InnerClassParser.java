package com.cd2cd.dom.java.parse;

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.*;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InnerClassParser {

    public static void parseInnerClass(InnerClass innerClass, String code) {

        String classH = getClassHeader(code);

        // visible
        String visible = getVisible(classH);
        innerClass.setVisibility(JavaVisibility.valueOf(visible.toUpperCase()));

        // 类注释

        // 类注解

        // 类继承-父类、范型、接口
        setSuperClass(innerClass, classH);


        // 类方法-注解、异常、返回值、参数

        //


        // 获取类名称

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
        if(m.find()) {
            return m.group();
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

        // 类有接口
        if(classH.indexOf(ii) > 0) {
            String[] ss = classH.split(ii);

            // 类接口部份
            implStr = ss[1];
            implStr = implStr.substring(0, implStr.lastIndexOf("{")).trim();

            // 类范型 + 类父类继承
            String firstStr = ss[0];

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
        }


        System.out.println("implStr=" +implStr +"\nsuperStr=" +superStr +"\nclassTypeStr=" +classTypeStr);

        // 类【范型】
//        setClassTypes(classTypeStr, innerClass);

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


    public static void main(String[] args) throws IOException {

        File file = new File("/Volumes/data/code-sources/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java");
        TopClassParser cf = new TopClassParser(file);

        TopLevelClass cc = cf.toTopLevelClass();
        System.out.println(cc.getFormattedContent());

    }
}
