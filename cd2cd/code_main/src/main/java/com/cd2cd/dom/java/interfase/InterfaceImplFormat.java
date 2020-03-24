package com.cd2cd.dom.java.interfase;

import com.cd2cd.dom.java.CodeUtils;
import com.cd2cd.dom.java.MyMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class InterfaceImplFormat {

    private List<MyMethod> methods = new ArrayList<>();
    private TopLevelClass mTopLevelClass;
    private Map<String, MyMethod> genMethodMap = new HashMap<>();

    public InterfaceImplFormat(String code, String defaultName) {

        if(StringUtils.isEmpty(code)) {
            mTopLevelClass = new TopLevelClass(defaultName);
            return;
        }

        // 生成包名、类名
        String pkg = CodeUtils.getPackage(code);
        String cName = CodeUtils.getClassName(code);
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("%s.%s", pkg, cName));
        mTopLevelClass = new TopLevelClass(type);
        mTopLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // 设置类的import
        Set<FullyQualifiedJavaType> importedTypes = CodeUtils.getImport(code);
        mTopLevelClass.addImportedTypes(importedTypes);

        // 设置类注释
        String javaDocLine = CodeUtils.getClassjavaDocLine("class", code);
        if(StringUtils.isNotBlank(javaDocLine)) {
            mTopLevelClass.addJavaDocLine(javaDocLine);
        }

        // 类的方法-注解、类成员变量-注解、静态块、内部类(枚举)、父类、接口
        genMethodMap = CodeUtils.getInterfaceImplMethodsAndSetClassProperties(mTopLevelClass, methods, code);
    }

    public List<MyMethod> getMethods() {
        return methods;
    }

    public TopLevelClass getmTopLevelClass() {
        return mTopLevelClass;
    }

    public Map<String, MyMethod> getGenMethodMap() {
        return genMethodMap;
    }

    public static void main(String[] args) throws IOException {

        String path = "/Volumes/data/code-sources/java-source/loan_admin/loan_admin_main/src/main/java/com/yishang/loan_admin/credit_trial/service/impl/CreditTrialServiceImpl.java";
        String s = IOUtils.toString(new FileInputStream(new File(path)), "utf-8");
        InterfaceImplFormat iff = new InterfaceImplFormat(s, "CreditTrialServiceImpl");
        TopLevelClass originImpl = iff.getmTopLevelClass();

        System.out.println(originImpl.getFormattedContent());

    }
}
