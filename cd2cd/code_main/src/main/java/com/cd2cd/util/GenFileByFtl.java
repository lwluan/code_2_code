package com.cd2cd.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * 根据模板文件生成 文件
 * 
 * @author leiwuluan
 *
 */
public class GenFileByFtl {
	private static final String TEMP_VERSION = "2.3.23";
	
	public void fetchResourcesFromJar(String tempFile, Map<String, Object> dataObj, OutputStream output) throws IOException, TemplateException {
		InputStream in = GenFileByFtl.class.getClassLoader().getResourceAsStream(tempFile);
		String templateContent = IOUtils.toString(in, Charsets.toCharset("utf-8"));

		StringTemplateLoader stringLoader = new StringTemplateLoader();

		String tempName = (Math.random() * 1000) + "";
		stringLoader.putTemplate(tempName, templateContent);

		Configuration config = new Configuration(new Version(TEMP_VERSION));
		config.setEncoding(Locale.CHINA, "utf-8");
		config.setTemplateLoader(stringLoader);

		Template template = config.getTemplate(tempName, "utf-8");
		Writer out = new OutputStreamWriter(output, "utf-8");
		template.process(dataObj, out);
	}
	
	public void genPageByFilePath(String tempFile, Map<String, Object> dataObj, String filePath) throws IOException, TemplateException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		fetchResourcesFromJar(tempFile, dataObj, baos);
		String pageStr = new String(baos.toByteArray());
		IOUtils.write(pageStr, new FileOutputStream(new File(filePath)), "utf-8");
	}
	
	public static void main(String[] args) {
		
	}

}
