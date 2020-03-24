package com.cd2cd.dom.java.parse;

/**
private List<Field> fields;
private List<InnerClass> innerClasses;
private List<InnerEnum> innerEnums;
private List<TypeParameter> typeParameters;
private FullyQualifiedJavaType superClass;
private FullyQualifiedJavaType type;
private Set<FullyQualifiedJavaType> superInterfaceTypes;
private List<Method> methods;
private boolean isAbstract;
private List<InitializationBlock> initializationBlocks;
*/

public enum LineType {
    Fields, // 成员变量
    InnerClasses, // 内部类
    InnerEnums, // 内部枚举
    Methods, // 方法
    InitializationBlock, // 静态块/非静态块
    Other, // 其他
}
