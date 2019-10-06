package com.cd2cd.dom.java;

import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaVisibility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InnerClassUtil {

    public static String getClassHeader(String code) {
        Pattern p = Pattern.compile("(public|private|protected)?.*(static)?.*class.+\\{");
        Matcher m = p.matcher(code);
        if(m.find()) {
            return m.group();
        }
        return null;
    }

    public static String getInnerClassName(String classH) {
        classH = classH.split("class")[1];
        classH = classH.substring(0, classH.indexOf("{"));
        classH = classH.trim();
        return classH;
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
            System.out.println("fieldStr=" + fieldStr);

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

    }

    public static void setClassMethod(InnerClass innerClass, String code) {

    }

    public static InnerClass formatInnerClass(String code) {
        code = code.trim();

        String classH = getClassHeader(code);
        System.out.println(code);


        // class name
        String className = getInnerClassName(classH);

        InnerClass innerClass = new InnerClass(className);

        // class comment
        setClassComment(innerClass, code);

        // class visible
        String vis = getVisible(classH);
        innerClass.setVisibility(JavaVisibility.valueOf(vis.toUpperCase()));

        // class field
        setClassFiled(innerClass, code);

        // class method
        setClassMethod(innerClass, code);

        // static initializationBlocks

        // set innerClass

        // set innerEnums

        // set superClass

        // set superInterfaceTypes

        // set typeParameters

        return innerClass;
    }
}
