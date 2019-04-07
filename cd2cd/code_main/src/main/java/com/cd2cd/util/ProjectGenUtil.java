package com.cd2cd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cd2cd.domain.ProDatabase;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.ProFunArg;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.ProTableColumn;
import com.cd2cd.util.mbg.h2.H2DatabaseUtil;

public class ProjectGenUtil {
	private static String code_path = "code_template";
	private static final Logger LOG = LoggerFactory.getLogger(ProjectUtils.class);
	private static final String H2_DB_PATH = "./h2db";
	private static final String H2_DB_PASSWORD = "h2";
	private static final String H2_DB_USER = "123456";
	
	private ProProject project;
	String contextPath;			// 项目访问地址
	String artifactId;			// test_main
	String artifactIdName;		// artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
	String groupId;				// 包名 com.test
	String name;				// 项目，中文名称
	String packageType;  		// 包结构类型：standard、module
	String version;				// 项目版本号
	String description;			// 项目描述
	String localPath;			// 本地生成路径
	
	public ProjectGenUtil(ProProject project) {
		this.project = project;
		
		contextPath = project.getContextPath();
		artifactId = project.getArtifactId();
		groupId = project.getGroupId();
		name = project.getName();
		packageType = project.getPackageType();
		version = project.getVersion();
		description = project.getDescription();
		localPath = project.getLocalPath();
		artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
	}

	public void genProjectBase() throws Exception {

		if (!new File(localPath).exists()) {
			throw new Exception("localPath=" + localPath + ",不存在");
		}

		String _path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "../../../../" + code_path;
		_path = _path.replace("file:", "");
		String tpPath = getRealPath(_path);

		// 1、项目复制
		File src = new File(tpPath);
		File dest = new File(localPath + "/" + code_path);
		if (!new File(localPath + "/" + artifactIdName).exists()) {
			copyFolder(src, dest);
		}
		// 2、项目替换
		replaceProject();
	}

	
	/**
	 * 替换文件内容
	 * 
	 * @param project
	 * @throws Exception
	 */
	private void replaceProject() throws Exception {

		String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

		String mainProjectName = artifactIdName + "_main";
		String parentProjectName = artifactIdName + "_parent";

		// 项目模板地址
		String projectPath = localPath + "/" + code_path;
		String targetRootPath = localPath + "/" + artifactIdName;
		reNameFile(projectPath, targetRootPath);
		projectPath = targetRootPath;

		String projectMainPath = projectPath + "/" + code_path + "_main";
		String targetMainPath = projectPath + "/" + mainProjectName;
		reNameFile(projectMainPath, targetMainPath);
		projectMainPath = targetMainPath;

		String projectParentPath = projectPath + "/" + code_path + "_parent";
		String targetParentPath = projectPath + "/" + parentProjectName;
		reNameFile(projectParentPath, targetParentPath);
		projectParentPath = targetParentPath;

		// 重命名项目包名
		String projectMainSrcGroupIdPath = targetMainPath + "/src/main/java/com/cd2cd";
		String targetMainSrcGroupIdPath = targetMainPath + "/src/main/java/" + groupIdName;

		// rename group id
		reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
		projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;

		// rename artifact id
		String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
		String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;

		reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
		projectMainSrcArtifactIdPath = targetMainSrcArtifactIdPaht;

		reNameFolder();
		replacePomXml();
		replaceJavaAndMapperXml();
	}
	
	private void replaceJavaAndMapperXml() throws Exception {

		String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
		String projectPath = localPath + "/" + artifactIdName;

		List<File> files = new ArrayList<File>();
		listAllFile(projectPath, files);

		String packageSrc = "package com.cd2cd.admin";
		String packageDesc = "package " + groupId + "." + artifactIdName;
		String importSrc = "import com.cd2cd.admin.";
		String importDesc = "import " + groupId + "." + artifactIdName + ".";

		for (File f : files) {

			if (f.getPath().endsWith(".java")) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content.replaceAll(packageSrc, packageDesc).replaceAll(importSrc, importDesc);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			} else if (f.getPath().endsWith(".properties")) {

				List<String> lines = IOUtils.readLines(new FileInputStream(f), "utf-8");
				StringBuffer content = new StringBuffer();
				for (String line : lines) {

					if (line.indexOf("mybatis.mapper-locations") > -1) {
						line = "mybatis.mapper-locations=classpath:" + groupIdName + "/" + artifactIdName + "/**/*Mapper.xml";
					} else if(line.indexOf("mybatis.type-aliases-package") > -1 ) {
						line = "mybatis.type-aliases-package=" + groupId + "." + artifactIdName + ".domain";
					} else if(line.indexOf("server.context-path") > -1 ) {
						line = "server.context-path=" + contextPath;
					}
					content.append(line);
					content.append("\r\n");
				}
				IOUtils.write(content.toString(), new FileOutputStream(f), "utf-8");
			} else if (f.getPath().endsWith("Mapper.xml")) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content.replaceAll("com.cd2cd.admin", groupId + "." + artifactIdName);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			} else if (f.getPath().endsWith(".html")) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content.replaceAll("/code_template", contextPath);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			}

		}
	}
	
	public void listAllFile(String filePath, List<File> files) {
		File file = new File(filePath);

		File[] files2 = file.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				else {
					String name = file.getName();
					if (name.endsWith(".java") || name.endsWith(".xml") || name.endsWith(".html")
							|| name.endsWith(".properties"))
						return true;
					else
						return false;
				}
			}
		});

		if (files2 != null) {
			for (File fi : files2) {
				if (fi.isDirectory()) {
					listAllFile(fi.getAbsolutePath(), files);
				}
				if (fi.isFile()) {
					files.add(fi);
				}
			}
		}
	}
	
	private void replacePomXml() throws Exception {

		// - = - = - = - = - 内容替换 - = - = - = - = -
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

		String mainProjectName = artifactIdName + "_main";
		String parentProjectName = artifactIdName + "_parent";

		// 项目模板地址
		String projectPath = localPath + "/" + artifactIdName;
		String projectMainPath = projectPath + "/" + mainProjectName;
		String projectParentPath = projectPath + "/" + parentProjectName;

		// root pom.xml
		String pomFilePath = projectPath + "/pom.xml";

		// pom.xml
		Document document = getDocumentByFilePath(pomFilePath);
		Element rootElement = document.getDocumentElement();

		getChildsByTagName(rootElement, "groupId").get(0).setTextContent(groupId);
		getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(artifactIdName);
		getChildsByTagName(rootElement, "version").get(0).setTextContent(version);
		getChildsByTagName(rootElement, "name").get(0).setTextContent(artifactIdName);

		Node modulesNode = getChildsByTagName(rootElement, "modules").get(0);
		cleanNodeChilds(modulesNode);

		Node moduleNode = document.createElement("module");
		moduleNode.setTextContent(mainProjectName);
		modulesNode.appendChild(moduleNode);

		moduleNode = document.createElement("module");
		moduleNode.setTextContent(parentProjectName);
		modulesNode.appendChild(moduleNode);

		saveDocument(document, pomFilePath);

		// main pom.xml
		pomFilePath = projectMainPath + "/pom.xml";
		document = getDocumentByFilePath(pomFilePath);
		rootElement = document.getDocumentElement();

		Node parentNode = getChildsByTagName(rootElement, "parent").get(0);
		getChildsByTagName(parentNode, "groupId").get(0).setTextContent(groupId);
		getChildsByTagName(parentNode, "artifactId").get(0).setTextContent(parentProjectName);
		getChildsByTagName(parentNode, "version").get(0).setTextContent(version);
		getChildsByTagName(parentNode, "relativePath").get(0).setTextContent("../" + parentProjectName);

		getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(mainProjectName);

		Node propertiesNode = getChildsByTagName(rootElement, "properties").get(0);
		getChildsByTagName(propertiesNode, "start-class").get(0).setTextContent(
				groupId + "." + artifactIdName + ".AppMainStarter");

		Node buildNode = getChildsByTagName(rootElement, "build").get(0);
		getChildsByTagName(buildNode, "finalName").get(0).setTextContent(mainProjectName);

		saveDocument(document, pomFilePath);

		// parent pom.xml
		pomFilePath = projectParentPath + "/pom.xml";
		document = getDocumentByFilePath(pomFilePath);
		rootElement = document.getDocumentElement();

		getChildsByTagName(rootElement, "groupId").get(0).setTextContent(groupId);
		getChildsByTagName(rootElement, "artifactId").get(0).setTextContent(parentProjectName);
		getChildsByTagName(rootElement, "version").get(0).setTextContent(version);

		saveDocument(document, pomFilePath);
	}
	
	private void cleanNodeChilds(Node node) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			node.removeChild(nodeList.item(0));
			i--;
		}
	}
	
	private List<Node> getChildsByTagName(Node node, String tagName) {
		NodeList nodeList = node.getChildNodes();
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			if (tagName.equals(nodeList.item(i).getNodeName())) {
				nodes.add(nodeList.item(i));
			}
		}
		return nodes;
	}
	
	private Document getDocumentByFilePath(String filePath) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(false);
		factory.setCoalescing(false);
		factory.setExpandEntityReferences(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream iis = new FileInputStream(new File(filePath));
		Document document = builder.parse(iis);
		return document;
	}

	private void saveDocument(Document document, String filePath) throws Exception {
		String contentStr = toStringFromDoc(document);
		IOUtils.write(contentStr, new FileOutputStream(filePath), "utf-8");
	}

	private String toStringFromDoc(Document document) {
		String result = null;

		if (document != null) {
			StringWriter strWtr = new StringWriter();
			StreamResult strResult = new StreamResult(strWtr);
			TransformerFactory tfac = TransformerFactory.newInstance();
			try {
				javax.xml.transform.Transformer t = tfac.newTransformer();
				t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				t.setOutputProperty(OutputKeys.INDENT, "yes");
				t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
				// text
				t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
				t.transform(new DOMSource(document.getDocumentElement()), strResult);
			} catch (Exception e) {
				System.err.println("XML.toString(Document): " + e);
			}
			result = strResult.getWriter().toString();
			try {
				strWtr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	private void reNameFolder() {

		String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");

		String mainProjectName = artifactIdName + "_main";
		String parentProjectName = artifactIdName + "_parent";

		// 项目模板地址
		String projectPath = localPath + "/" + code_path;
		String targetRootPath = localPath + "/" + artifactIdName;
		reNameFile(projectPath, targetRootPath);
		projectPath = targetRootPath;

		String projectMainPath = projectPath + "/" + code_path + "_main";
		String targetMainPath = projectPath + "/" + mainProjectName;
		reNameFile(projectMainPath, targetMainPath);
		projectMainPath = targetMainPath;

		String projectParentPath = projectPath + "/" + code_path + "_parent";
		String targetParentPath = projectPath + "/" + parentProjectName;
		reNameFile(projectParentPath, targetParentPath);
		projectParentPath = targetParentPath;

		// rename group id
		String projectMainSrcGroupIdPath = targetMainPath + "/src/main/java/com/cd2cd";
		String targetMainSrcGroupIdPath = targetMainPath + "/src/main/java/" + groupIdName;
		reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
		projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;

		// rename artifact id
		String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
		String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;
		reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
		projectMainSrcArtifactIdPath = targetMainSrcArtifactIdPaht;

	}
	
	private void reNameFile(String f1, String f2) {
		File file1 = new File(f1);
		File file2 = new File(f2);

		if (file1.exists() && !file2.exists()) {
			file1.renameTo(file2);
		}
	}
	
	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 */
	private void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			IOUtils.copy(in, out);
		}
	}

	
	/**
	 * 将相对路径替换成决对路径
	 * @param path
	 * @return
	 */
	private String getRealPath(String path) {
		int n = path.indexOf("../");
		if (n > -1) {
			String eStr = path.substring(n + 3, path.length());
			String sStr = path.substring(0, n - 1);
			sStr = sStr.substring(0, sStr.lastIndexOf("/") + 1);
			String p = sStr + eStr;
			path = p;
			if (path.indexOf("../") > -1) {
				path = getRealPath(path);
			}
		}
		return path;
	}

	public void initH2Database(List<ProTable> tables, ProDatabase database) throws SQLException {
		String dbName = database.getDbName();
		String user = H2_DB_USER;
		String password = H2_DB_PASSWORD;
		
		H2DatabaseUtil nH2DatabaseUtil = new H2DatabaseUtil(H2_DB_PATH, dbName, user, password);
		
		for(ProTable t: tables) {
			String dropTable = "DROP TABLE IF EXISTS " + t.getName();
			nH2DatabaseUtil.exeSchema(dropTable);
			
			
			StringBuilder createTab = new StringBuilder();
			createTab.append("CREATE TABLE IF NOT EXISTS ");
			createTab.append(t.getName());
			createTab.append("(");
			List<ProTableColumn> columns = t.getColumns();
			List<String> _columnAndType = new ArrayList<String>();
			
			String primaryKeyColumn = null;
			for(ProTableColumn c: columns) {
				
				_columnAndType.add(c.getName() + " " + c.getMysqlType());
				if("PRI".equalsIgnoreCase(c.getKeyType())) {
					primaryKeyColumn = c.getName();
				}
			}
			if(StringUtils.isNotBlank(primaryKeyColumn)) {
				_columnAndType.add("PRIMARY KEY (`"+primaryKeyColumn+"`)");
			}
			createTab.append(Strings.join(_columnAndType, ','));
			createTab.append(")");
			
			System.out.println("createTab=" + createTab);
			nH2DatabaseUtil.exeSchema(createTab.toString());
		}
	}

	public void genJavaFromDb(List<ProTable> tables, ProDatabase database) {
		try {
			
			String artifactId = project.getArtifactId();
			String groupId = project.getGroupId();
			String name = project.getName();
			String packageType = project.getPackageType();
			String localPath = project.getLocalPath();
			localPath = localPath.endsWith("/") ? localPath : localPath + "/";
			localPath = localPath + artifactId + "/" + artifactId + "_main/";
			
			// ${driverClass} com.mysql.jdbc.Driver
			// ${connectionURL} jdbc:mysql://127.0.0.1:3306/auto_code
			
			// ${javaModel}com.cd2cd.domain
			// ${sqlMap} com.cd2cd.mapper
			// ${javaClient} com.cd2cd.mapper
			
			// ${targetProject}/src
			
			// table
			
			String sqlMap = groupId + "." + artifactId + ".mapper";
			String javaModel = groupId + "." + artifactId + ".domain";;
//			String connectionURL = "jdbc:mysql://" + database.getHostname() + ":" + database.getPort() + "/" + database.getDbName();
			
			// h2
			String connectionURL = "jdbc:h2:"+H2_DB_PATH+"/" + database.getDbName();
			
			
			ByteArrayOutputStream bai = new ByteArrayOutputStream();
			GenFileByFtl mGenFileByFtl = new GenFileByFtl();
			Map<String, Object> dataObj = new HashMap<String, Object>();
			
			dataObj.put("userId", H2_DB_USER);
			dataObj.put("password", H2_DB_PASSWORD);
			
//			dataObj.put("driverClass", "com.mysql.jdbc.Driver");
			dataObj.put("driverClass", "org.h2.Driver");
			dataObj.put("connectionURL", connectionURL);
			dataObj.put("javaModel", javaModel);
			dataObj.put("sqlMap", sqlMap);
			dataObj.put("javaClient", sqlMap);
			dataObj.put("targetProject", localPath);
			
			dataObj.put("tables", tables);
			
			mGenFileByFtl.fetchResourcesFromJar("gen_tables_template.xml.ftl", dataObj, bai);
			
			
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			
			Configuration config = cp.parseConfiguration(new ByteArrayInputStream(bai.toByteArray()));
			DefaultShellCallback shellCallback = new DefaultShellCallback(true);
			
			
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
			Set<String> ids = new HashSet<String>();
			ids.add("tables");
			
			myBatisGenerator.generate(null, ids);
		} catch (Exception e) {
			LOG.error("error={}", e.getMessage(), e);
		}
	}

	public void genController(ProProject proProject, List<ProFile> controllerList) throws Exception {
		
		
		// /Users/lwl/Desktop/test_project/test_pro/test_pro_main/src/main/java/com/test/test_pro/controller/
		// /Users/lwl/Desktop/test_project/test_pro/user/controller/
		for(ProFile file : controllerList) {
			
			ProModule module = file.getModule();
			String fileGenPath = (localPath + "/" + artifactId + "/"+artifactId+"_main/src/main/java/" + groupId + "." + artifactId).replaceAll("\\.", "/");
			String filePkg = groupId + "." + artifactId;
			String fileClassPath = filePkg + ".controller." + file.getName();
			
			if( PackageTypeEnum.Flat.name().equals(packageType) ) {
				fileGenPath += "/controller/" + file.getName() + ".java";
			} else { // 模块化
				fileGenPath += "/" + module.getName() + "/controller/" + file.getName() + ".java";
				fileClassPath = filePkg + "." + module.getName() + ".controller." + file.getName();
			}
			
			FullyQualifiedJavaType controllerType = new FullyQualifiedJavaType(fileClassPath);
			TopLevelClass topClass = new TopLevelClass(controllerType);
			topClass.setVisibility(JavaVisibility.PUBLIC);
			topClass.addFileCommentLine("/** \n" + file.getComment() + "\n **/");
			
			topClass.addAnnotation("@Controller");
			topClass.addAnnotation("@RequestMapping(\""+file.getReqPath()+"\")");
			
			/**
			 *  import class
			 */
			topClass.addImportedType(RequestMapping.class.getName());
			topClass.addImportedType(Controller.class.getName());
			topClass.addImportedType(ResponseBody.class.getName());
			topClass.addImportedType(Valid.class.getName());
			
			
			/**
			 * 生成方法，参数，返回值
			 **/
			for(ProFun fun : file.getFuns()) {
				Method m = new Method(fun.getFunName());
				m.setVisibility(JavaVisibility.PUBLIC);
				
				
				/**
				 * 方法注解
				 */
				m.addAnnotation("@ResponseBody");
				m.addAnnotation("@RequestMapping(\""+fun.getReqPath()+"\")");
				
				
				/**
				 * 方法参数
				 */
				List<ProFunArg> args = fun.getArgs();
				for(ProFunArg arg : args) {
					Parameter mp = new Parameter(new FullyQualifiedJavaType(arg.getArgTypeName()), arg.getName());
					
					if(HttpMethod.POST.name().equalsIgnoreCase(fun.getReqMethod())) {
						mp.addAnnotation("@RequestBody");
					}
					mp.addAnnotation("@Valid");
					m.addParameter(mp);
				}
				/**
				 * 方法返回值
				 */
				if(StringUtils.isNotBlank(fun.getReturnShow())) {
					m.setReturnType(new FullyQualifiedJavaType(fun.getReturnShow()));
				}
				
				/**
				 * todo
				 */
				m.addBodyLine("// TODO ");
				
				topClass.addMethod(m);
			}
			
			
			System.out.println(topClass.getFormattedContent());
			// 生成文件
			File genFile = new File(fileGenPath);
			if( ! genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			IOUtils.write(topClass.getFormattedContent(), new FileOutputStream(new File(fileGenPath)), "utf-8");
		}
	}
	
	public static void main(String[] args) {
		FullyQualifiedJavaType fjt = new FullyQualifiedJavaType("com.cd2cd.controller.UserController");
		TopLevelClass topClass = new TopLevelClass(fjt);
		topClass.setVisibility(JavaVisibility.PUBLIC);
		topClass.addFileCommentLine("/** \n 文件注释 \n @author leiwuluan \n @time 2019-04-07 \n **/");
		
		topClass.addAnnotation("@Controller");
		topClass.addAnnotation("@RequestMapping(\"user\")");
		
		topClass.addImportedType("org.springframework.web.bind.annotation.*");
		
		Method m = new Method("getUserInfo");
		m.setVisibility(JavaVisibility.PUBLIC);
		m.setReturnType(new FullyQualifiedJavaType("BaseRes<User>"));
		m.addBodyLine("//TODO");
		
		Parameter mp = new Parameter(new FullyQualifiedJavaType("UserInfo"), "userInfo");
		mp.addAnnotation("@ResquestBody");
		mp.addAnnotation("@Valid");
		m.addParameter(mp);
		
		m.addAnnotation("@ResponseBody");
		m.addAnnotation("@RequestMapping(\"userInfo\")");
		
		topClass.addMethod(m);
		System.out.println(topClass.getFormattedContent());
		
	}
}




