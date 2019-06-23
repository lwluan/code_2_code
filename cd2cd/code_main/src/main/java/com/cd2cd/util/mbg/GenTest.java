package com.cd2cd.util.mbg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.JavaTypeResolver;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.types.JdbcTypeNameTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成主类文件
 *
 */
public class GenTest {
	
	private static Logger LOG = LoggerFactory.getLogger(GenTest.class);
	public static void main(String[] args) {
		GenTest generator = new GenTest();
//		generator.process("file/zp_tenant_gen_tables.xml");
//		generator.process("file/template_gen_tables.xml");
//		generator.process("file/auto_code_gen_tables.xml");
//		generator.process("file/conn_gen_tables.xml");
		
		String s = "datetime";
//		String s = "int(11) unsigned";
		int ei = s.indexOf("(");
		System.out.println(s.substring(0, ei>-1?ei:s.length()));
	}
	
	public void process(String fileName) {
		try {
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			
			InputStream is = GenTest.class.getClassLoader().getResourceAsStream(fileName);

			Configuration config = cp.parseConfiguration(is);
			DefaultShellCallback shellCallback = new DefaultShellCallback(true); // true
			
			
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
			Set<String> ids = new HashSet<String>();
			ids.add("tables");
			
			myBatisGenerator.generate(null, ids);
		} catch (Exception e) {
			LOG.error("Exception:", e);
			e.printStackTrace();
		}
	}

}
