package com.cd2cd.dom.java.parse;

import com.cd2cd.dom.java.InnerClassUtil;
import org.apache.commons.io.IOUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TopClassParser {

    private String code;
    private TopLevelClass topLevelClass;

    public TopClassParser(String code) {
        this.code = code;
        initClass();
    }

    public TopClassParser(File file) throws IOException {
        String code = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);
        this.code = code;
        initClass();
    }

    private void initClass() {
        String className = this.getClassName();

        System.out.println("className=" + className);

        String pkName = getPackage();
        this.topLevelClass = new TopLevelClass(String.format("%s.%s", pkName, className));

        /**
         * package
         * importedTypes
         * staticImports
         * fileCommentLines
         */

        setFileComment();
        setImport();
        InnerClassParser.parseInnerClass(topLevelClass, this.code);
    }

    private String getPackage() {
        String pkName = "package";
        Pattern p = Pattern.compile(pkName+"\\s+.+;");
        Matcher m = p.matcher(this.code);
        if(m.find()) {
            String cName = m.group();
            cName = cName.substring(cName.indexOf(pkName)+pkName.length(), cName.indexOf(";"));
            cName = cName.trim();
            return cName;
        }
        return "";
    }

    private void setImport() {

        String pStr = "import";
        Pattern p = Pattern.compile(pStr+"\\s+.+;");
        Matcher m = p.matcher(this.code);
        while(m.find()) {
            String s = m.group();
            s = s.substring(s.indexOf(pStr)+pStr.length(), s.indexOf(";"));
            s = s.trim();
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(s);
            this.topLevelClass.getImportedTypes().add(type);
        }
    }

    private void setFileComment() {
        String commLine = this.code.substring(0, this.code.indexOf("package"));
        String ls = System.getProperty("line.separator"); //$NON-NLS-1$
        if (ls == null) {
            ls = "\n"; //$NON-NLS-1$
        }
        String[] lines = commLine.split(ls);
        this.topLevelClass.getFileCommentLines().addAll(Arrays.asList(lines));
    }

    private String getClassName() {
        String classH = InnerClassParser.getClassHeader(this.code);
        String className = InnerClassUtil.getInnerClassName(classH);
        return className;
    }

    public TopLevelClass toTopLevelClass() {
        return topLevelClass;
    }
    public static void main(String[] args) throws IOException {

        String url = "/Users/lwl/Documents/source-code/java-code/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java";
//        url = "/Volumes/data/code-sources/java-source/code_2_code/cd2cd/code_main/src/main/java/com/cd2cd/dom/java/demo/DemoClass.java";
        File file = new File(url);
        TopClassParser cf = new TopClassParser(file);

        TopLevelClass cc = cf.toTopLevelClass();
        System.out.println(cc.getFormattedContent());

    }
}
