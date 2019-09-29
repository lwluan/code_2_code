package com.cd2cd.dom.java.interfase;

import com.cd2cd.dom.java.CodeUtils;
import com.cd2cd.dom.java.MyMethod;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

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
        String cName = CodeUtils.getInterfaceName(code);
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("%s.%s", pkg, cName));
        mTopLevelClass = new TopLevelClass(type);
        mTopLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // 设置类的import
        Set<FullyQualifiedJavaType> importedTypes = CodeUtils.getImport(code);
        mTopLevelClass.addImportedTypes(importedTypes);

        // 设置类注释
        String javaDocLine = CodeUtils.getClassjavaDocLine(code);
        if(StringUtils.isNotBlank(javaDocLine)) {
            mTopLevelClass.addJavaDocLine(javaDocLine);
        }

        // 类的方法-注解
        genMethodMap = CodeUtils.getInterfaceMethods(methods, code);
        mTopLevelClass.getMethods().addAll(methods);

        // 类成员变量-注解

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
}
