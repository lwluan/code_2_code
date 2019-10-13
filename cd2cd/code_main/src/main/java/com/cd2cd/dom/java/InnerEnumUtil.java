package com.cd2cd.dom.java;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerEnum;

public class InnerEnumUtil {

    public static InnerEnum formatInnerEnum(String code) {
        InnerEnum innerEnum = new InnerEnum(new FullyQualifiedJavaType("test00001"));

        return innerEnum;
    }

}
