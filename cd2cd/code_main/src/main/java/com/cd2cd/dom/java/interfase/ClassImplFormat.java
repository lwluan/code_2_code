package com.cd2cd.dom.java.interfase;

import org.apache.commons.io.IOUtils;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClassImplFormat {

    private String code;
    private TopLevelClass topLevelClass;

    public ClassImplFormat(String code) {
        this.code = code;
    }

    public ClassImplFormat(File file) throws IOException {
        String code = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);
        this.code = code;
    }

    private String getClassName() {
        return "aaaa";
    }

    public TopLevelClass toTopLevelClass() {
        String className = this.getClassName();
        TopLevelClass topLevelClass = new TopLevelClass(className);

        System.out.println(code);


        return topLevelClass;
    }
    public static void main(String[] args) throws IOException {

        File file = new File("/Volumes/data/code-sources/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java");
        ClassImplFormat cf = new ClassImplFormat(file);

        TopLevelClass cc = cf.toTopLevelClass();
        System.out.println(cc.getFormattedContent());

    }

}
