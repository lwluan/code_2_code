package com.cd2cd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.Field;
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
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONObject;
import com.cd2cd.dom.java.ClassFile;
import com.cd2cd.dom.java.CodeUtils;
import com.cd2cd.dom.java.FileIdsAndType;
import com.cd2cd.dom.java.GenServiceHelper;
import com.cd2cd.dom.java.TypeEnum.CollectionType;
import com.cd2cd.dom.java.TypeEnum.FieldDataType;
import com.cd2cd.dom.java.TypeEnum.FunArgType;
import com.cd2cd.dom.java.TypeEnum.ProjectModulTypeEnum;
import com.cd2cd.domain.ProDatabase;
import com.cd2cd.domain.ProField;
import com.cd2cd.domain.ProFile;
import com.cd2cd.domain.ProFun;
import com.cd2cd.domain.ProFunArg;
import com.cd2cd.domain.ProModule;
import com.cd2cd.domain.ProProject;
import com.cd2cd.domain.ProTable;
import com.cd2cd.domain.ProTableColumn;
import com.cd2cd.util.mbg.h2.H2DatabaseUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Sets;

public class ProjectGenUtil {
	private static String code_path = "code_template";
	private static final Logger LOG = LoggerFactory.getLogger(ProjectGenUtil.class);
	private static final String H2_DB_PATH = "./h2db";
	private static final String H2_DB_PASSWORD = "h2";
	private static final String H2_DB_USER = "123456";
	private static String NEW_LINE = System.getProperty("line.separator");
	
	private static Set<String> IGNORE_VO_GEN = Sets.newHashSet("BaseRes", "BaseReq");
	private static Set<String> _n = Sets.newHashSet(".DS_Store", ".project", ".settings", "target", "node_modules");
	
	private ProProject project;
	String contextPath;			// 项目访问地址
	String artifactId;			// test_main
	String artifactIdName;		// artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
	String groupId;				// 包名 com.test
	String name;				// 项目; crm-admin英文名称
	String packageType;  		// 包结构类型：standard、module
	String version;				// 项目版本号
	String description;			// 项目描述
	String localPath;			// 本地生成路径
	String basePkgname;			// groupId + artifactId
	
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
		
		basePkgname = groupId + "." + artifactId.replaceAll("-", "_");
	}

	public void genProjectBase() throws Exception {

		if (!new File(localPath).exists()) {
			new File(localPath).mkdirs();
		}

		String _path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "../../../../" + code_path;
		_path = _path.replace("file:", "");
		String tpPath = getRealPath(_path);
		
		LOG.info("_path={}, tpPath={}", _path, tpPath);

		// 1、项目复制
		File src = new File(tpPath);
		File dest = new File(localPath + "/" + code_path);
		if (!new File(localPath + "/" + artifactIdName).exists()) {
			copyFolder(src, dest);
		}
		// 2、项目替换
		LOG.info("replaceProject ...");
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

		LOG.info("files-size={}", files.size());
		for (File f : files) {

			LOG.info("f-name={}", f.getAbsoluteFile());
			
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
					content.append(NEW_LINE);
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
	
	private void listAllFile(String filePath, List<File> files) {
		File file = new File(filePath);

		// 不替目录下的文件
		if(file.getName().equalsIgnoreCase("node_modules") || file.getName().equalsIgnoreCase("target")) {
			return;
		}
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
		getChildsByTagName(rootElement, "name").get(0).setTextContent(mainProjectName);
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
		
		LOG.info("copyFolder src={}, dest={}", src.getAbsoluteFile(), dest.getAbsoluteFile());
		
		if(_n.contains(src.getName())) {
			LOG.info("ignore file [{}]", src.getName());
			return;
		}
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
		LOG.info("path={}", path);
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
				
				/**
				 * float(6,3) 会出错，直接生成时，为float 
				 */
				String mysqlType = c.getMysqlType();
				if(mysqlType.toLowerCase().startsWith("float")) {
					mysqlType = "float";
				}
				_columnAndType.add(c.getName() + " " + mysqlType);
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
			
			
			/**
			 * 自动生成设置 primary key
			 * 1、auto_increment
			 * 2、单个 PRI
			 * 3、多个不设置
			 */
			tables.stream().forEach(tab->{
				List<ProTableColumn> columns = tab.getColumns();
				String extraColumn = null;
				String priColumn = null;
				int priNum = 0;
				for(ProTableColumn c: columns) {
					if("auto_increment".equalsIgnoreCase(c.getExtra())) {
						extraColumn = c.getName();
						break;
					} else if ("PRI".equalsIgnoreCase(c.getKeyType())){
						priColumn = c.getName();
						priNum ++;
					}
				}
				if(null != extraColumn) {
					tab.setIdentityPrimaryKey(extraColumn);
				} else if(priNum == 1 && priColumn != null) {
					tab.setIdentityPrimaryKey(priColumn);
				}
			});
			
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

	public void genController(ProProject proProject, List<ProFile> controllerList, Map<String, String> commValidMap) throws Exception {
		
		Map<Long, Method> methodMap = new HashMap<>();
		
		for(ProFile file : controllerList) {
			
			ProModule module = file.getModule();
			
			String fgp = proProject.getClassRootPath();
			String fcp = proProject.getClassRootPkg();
			String moduleName = module.getName();
			String packageType = proProject.getPackageType();
			String pkgName = "controller";
			String className = file.getName();
			ClassFile mClassFile = new ClassFile(fgp, fcp, moduleName, packageType, pkgName, className);
			
			String fileGenPath = mClassFile.getFileGenPath();
			TopLevelClass topClass = mClassFile.getType();
			
			topClass.setVisibility(JavaVisibility.PUBLIC);
			if(StringUtils.isNotBlank(file.getComment())) {
				topClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
			}
			
			topClass.addAnnotation("@Controller");
			if(StringUtils.isNotBlank(file.getReqPath())) {
				topClass.addAnnotation("@RequestMapping(\""+file.getReqPath()+"\")");
			}
			
			/**
			 *  import class
			 */
			topClass.addImportedType(RequestMapping.class.getName());
			topClass.addImportedType(Controller.class.getName());
			topClass.addImportedType(ResponseBody.class.getName());
			topClass.addImportedType(RequestBody.class.getName());
			topClass.addImportedType(Validated.class.getName());
			topClass.addImportedType(RequestMethod.class.getName());
			
			
			LOG.info("file.getImportTypes()={}", String.join(",", file.getImportTypes()));
			Map<String, String> importTypePath = new HashMap<>();
			for(String importedType : file.getImportTypes()) {
				topClass.addImportedType(importedType);
				importTypePath.put(importedType.substring(importedType.lastIndexOf(".")+1), importedType);
			}
			
			methodMap.clear();
			/**
			 * 生成方法，参数，返回值
			 **/
			for(ProFun fun : file.getFuns()) {
				Method m = new Method(fun.getFunName());
				methodMap.put(fun.getId(), m);
				
				m.setVisibility(JavaVisibility.PUBLIC);
				
				String validGroupName = String.format("%s_%s", file.getName(), fun.getFunName());
				
				
				
				m.addJavaDocLine("/**");
				m.addJavaDocLine(" * "+ CodeUtils.getFunIdenStr(fun.getId()+""));
				m.addJavaDocLine(" * "+fun.getName());
				m.addJavaDocLine(" * "+fun.getComment());
				
				/**
				 * 方法注解
				 */
				m.addAnnotation("@ResponseBody");
				m.addAnnotation("@RequestMapping(value=\""+fun.getReqPath()+"\", method=RequestMethod."+fun.getReqMethod()+")");
				
				/**
				 * 方法参数
				 */
				List<ProFunArg> args = fun.getArgs();
				boolean isPostAndReqBody = false;
				for(ProFunArg arg : args) {
					
					String paramName = arg.getName();
					paramName = paramName.replace("{", "").replace("}", "");
					Parameter mp = new Parameter(new FullyQualifiedJavaType(arg.getArgTypeName()), paramName);
					
					m.addJavaDocLine(" * @param "+paramName);
					
					/**
					 * add (Valid RequestBody Validated) if arg type is Vo<br>
					 * base Valid if arg type of base
					 */
					if(FunArgType.vo.name().equals(arg.getArgType())) {
						if(HttpMethod.POST.name().equalsIgnoreCase(fun.getReqMethod()) || 
								HttpMethod.PUT.name().equalsIgnoreCase(fun.getReqMethod()) || 
								HttpMethod.DELETE.name().equalsIgnoreCase(fun.getReqMethod())) {
							mp.addAnnotation("@RequestBody");
							isPostAndReqBody = true;
						}
						if(checkVoHasValid(arg)) {
							mp.addAnnotation(String.format("@Validated(%s.class)", validGroupName));
							// com.aaa.test.vo.TestVo.Controller_fun.class
							String vgPath = importTypePath.get(arg.getArgTypeName());
							if(StringUtils.isNotEmpty(vgPath)) {
								vgPath = vgPath.substring(0, vgPath.lastIndexOf(".")) + ".gen.Super" + arg.getArgTypeName(); 
								vgPath = String.format("%s.%s", vgPath, validGroupName);
								topClass.addImportedType(vgPath);
							}
						}
					} else if(FunArgType.base.name().equals(arg.getArgType())
							// && StringUtils.isNotEmpty(arg.getValid())
							) {
						
						/**
						 * rest full直持 例: /aap/{id} @PathVariable("id") String id
						 */
						if(arg.getName().startsWith("{")) {
							mp.addAnnotation(String.format("@PathVariable(\"%s\")", paramName));
							topClass.addImportedType(PathVariable.class.getName());
						}
						
						/** TODO 无法在controller fun 参数在验证, 添加拦截器验证，验证方法上
						// 单独验证
						JSONArray arr = JSONArray.parseArray(arg.getValid());
						
						for(int i=0; i<arr.size(); i++) {
							String key = arr.getString(i);
							mp.addAnnotation(String.format("@%s", key));
							String vName = key.substring(0, key.indexOf("("));
							String vCPath = commValidMap.get(vName);
							topClass.addImportedType(vCPath);
						}
						**/
					}
					
					m.addParameter(mp);
				}
				
				/**
				 * 需要数据验证时，添加 BindingResult bindingResult
				 */
				if(args.size() > 0 && isPostAndReqBody) {
					Parameter mp = new Parameter(new FullyQualifiedJavaType(BindingResult.class.getName()), "bindingResult");
					m.addParameter(mp);
					topClass.addImportedType(BindingResult.class.getName());
				}
				
				
				/**
				 * todo
				 */
				m.addBodyLine("// TODO ");
				
				// function end
				m.addJavaDocLine("**/");
				
				/**
				 * 方法返回值
				 */
				if(StringUtils.isNotBlank(fun.getReturnShow())) {
					LOG.info("returnShow={}", fun.getReturnShow());
					m.setReturnType(new FullyQualifiedJavaType(fun.getReturnShow()));
					
					/**
					 * 默认返回 null
					 */
					m.addBodyLine("return null;");
				}
				
				topClass.addMethod(m);
			}
			
			File genFile = new File(fileGenPath);
			if( ! genFile.getParentFile().exists()) {
				genFile.getParentFile().mkdirs();
			}
			
			if( ! genFile.exists()) {
				IOUtils.write(topClass.getFormattedContent(), new FileOutputStream(new File(fileGenPath)), "utf-8");
			} else {
				String classTxt = IOUtils.toString(new FileInputStream(genFile), "utf-8");

				// 添加 import; 1先获取原来import,2再判断加入
				Set<String> newImports = OutputUtilities.calculateImports(topClass.getImportedTypes());
				classTxt = CodeUtils.updateClassImport(classTxt, newImports);
				
				// 更新类 root path
				
				
				// 更新类注释
				
				
				String preFunIden = null;
				for(Long funId: methodMap.keySet()) {
					
					String iden = CodeUtils.getFunIdenStr(funId+"");
					Method m = methodMap.get(funId);
					if(classTxt.indexOf(iden) > -1) {
						// update fun
						String funHeader = CodeUtils.getFunHeaderByIdentify(classTxt, iden);
						
						m.getBodyLines().clear();
						
						String mStr = m.getFormattedContent(1, false, topClass);
						mStr  = mStr.replace("abstract ", "");
						mStr = mStr.substring(mStr.indexOf(iden) + iden.length());
						
						mStr = mStr.substring(0, mStr.length() -1) + " {" + NEW_LINE;
						classTxt = classTxt.replace(funHeader, mStr);
					} else {
						String mStr = m.getFormattedContent(1, false, topClass);
						
						// 类当中无方法存在时，直接添加到类的底部
						if(preFunIden == null) {
							int lastIndex = classTxt.lastIndexOf("}");
							String sTmpStr = classTxt.substring(0, lastIndex);
							String eTmpStr = NEW_LINE+"}";
							
							classTxt = sTmpStr + mStr + eTmpStr;
						} else {
							// add fun
							String funHeader = CodeUtils.getFunHeaderByIdentify(classTxt, preFunIden);
							
							String sTmpStr = classTxt.substring(0, classTxt.indexOf(funHeader) + funHeader.length());
							String eTmpStr = classTxt.substring(classTxt.indexOf(funHeader) + funHeader.length());
							
							int funEndIndex = CodeUtils.findFunEndIndex(eTmpStr) + 1;
							String preFunBody = eTmpStr.substring(0, funEndIndex);
							eTmpStr = eTmpStr.substring(funEndIndex, eTmpStr.length());
							classTxt = sTmpStr + preFunBody + NEW_LINE + mStr + eTmpStr;
						}
					}
					
					preFunIden = iden;
				}
				
				// 回写内容
				IOUtils.write(classTxt, new FileOutputStream(new File(fileGenPath)), "utf-8");
			}
		}		
	}
	
	/** 生成 Service
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public void genService(ProProject project, List<ProFile> controllerList) throws FileNotFoundException, IOException {
	
		/**
		 * 需要生成 接口文件和实现文件
		 * controller 需要有funcation 和 参数
		 */
		for(ProFile file : controllerList) {
			
			ProModule module = file.getModule();
			
			String fgp = project.getClassRootPath();
			String fcp = project.getClassRootPkg();
			String moduleName = module.getName();
			String packageType = project.getPackageType();
			String pkgName = "service";
			String className = file.getName();
			ClassFile classFile = new ClassFile(fgp, fcp, moduleName, packageType, pkgName, className);
			
			GenServiceHelper mGenServiceHelper = new GenServiceHelper(classFile, file);
			mGenServiceHelper.genCode();
			
		}
		
	}
	
	public String getVoClassPath(ProFile f) {
		String pkgName = basePkgname;
		if( ProjectModulTypeEnum.module.name().equals(packageType) && f.getModule() != null) {
			pkgName += "." + f.getModule().getName() + "."+f.getFileType()+"." + f.getName();
		} else {
			pkgName += "."+f.getFileType()+"." + f.getName();
		}
		return pkgName;
	}
	
	private boolean checkVoHasValid(ProFunArg arg) {
		if(arg.getChildren() != null) {
			for(ProFunArg a: arg.getChildren()) {
				if(StringUtils.isNotEmpty(a.getValid())) {
					return true;
				}
			}
		}
		return false;
	}

	public FileIdsAndType getFunReturnType(String returnVoJson) {
		// {"name":"BaseRes","id":3,"collectionType":"single","paradigm":"yes","next":{"name":"TestVO","id":6,"collectionType":"map","paradigm":"no"}}
		FileIdsAndType frt = new FileIdsAndType();
		if(StringUtils.isNotBlank(returnVoJson)) {
			JSONObject obj = JSONObject.parseObject(returnVoJson);
			parseFunReturnType(frt, obj);
		}
		return frt;
	}
	
	private void parseFunReturnType(FileIdsAndType frt, JSONObject obj) {
		String collectionType = obj.getString("collectionType");
		Long objId = obj.getLong("id");
		
		frt.getTypeIds().add(objId);
		
		String type = getCollectionClassPathByName(collectionType);
		if(type != null) frt.getTypePaths().add(type);
		
		if(obj.containsKey("next")) {
			parseFunReturnType(frt, obj.getJSONObject("next"));
		}
	}
	
	private String getCollectionClassPathByName(String collectionType) {
		if(CollectionType.list.name().equalsIgnoreCase(collectionType)) {
			return List.class.getName();
		} else if(CollectionType.map.name().equalsIgnoreCase(collectionType)) {
			return Map.class.getName();
		} else if(CollectionType.set.name().equalsIgnoreCase(collectionType)) {
			return Set.class.getName();
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		// https://gitee.com/lwlgit/open_crm.git
		String remotePath = "https://gitee.com/lwlgit/open_crm.git";
		String proPath = "/Users/lwl/Desktop/open_crm";
		
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new
                UsernamePasswordCredentialsProvider("lwlgit","git123456");

        //克隆代码库命令
        CloneCommand cloneCommand = Git.cloneRepository();

		try {
			Git git = cloneCommand.setURI(remotePath) // 设置远程URI
					.setBranch("master") // 设置clone下来的分支
					.setDirectory(new File(proPath)) // 设置下载存放路径
					.setCredentialsProvider(usernamePasswordCredentialsProvider) // 设置权限验证
					.call();
			
	        System.out.print(git.tag());
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 返回类文件路径, 根据module 和包结构类型
	 * @param fileList
	 * @return
	 */
	public Set<String> getFileTypes(List<ProFile> fileList) {
		Set<String> types = new HashSet<String>();
		for(ProFile f : fileList) {

			// f.getFileType() controller|service|vo|dao|domain
			String pkgName = basePkgname;
			if( ProjectModulTypeEnum.module.name().equals(packageType) && f.getModule() != null) {
				pkgName += "." + f.getModule().getName() + "."+f.getFileType()+"." + f.getName();
			} else {
				pkgName += "."+f.getFileType()+"." + f.getName();
			}
			types.add(pkgName);
		}
		return types;
	}

	/**
	 * 获取参数中的，import type 和vo id
	 * @param args
	 * @return
	 */
	public FileIdsAndType getArgTypes(List<ProFunArg> args) {
		FileIdsAndType fit = new FileIdsAndType();
		parseArgType(args, fit);
		return fit;
	}
	
	private void parseArgType(List<ProFunArg> args, FileIdsAndType fit) {
		for(ProFunArg arg : args) {
			if(null != arg.getArgTypeId() && 0 < arg.getArgTypeId()) {
				fit.getTypeIds().add(arg.getArgTypeId());
			}
			String type = getCollectionClassPathByName(arg.getCollectionType());
			if(type != null) fit.getTypePaths().add(type);
			
			if( ! CollectionUtils.isEmpty(arg.getChildren())) {
				parseArgType(arg.getChildren(), fit);
			}
		}
	}

	/**
	 * 生成 vo类
	 * @param proProject
	 * @param voList
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void genVo(ProProject proProject, List<ProFile> voList, Map<String, String> commValidMap) throws FileNotFoundException, IOException {
		for(ProFile file : voList) {
			
			// ignore vo BaseRes BaseReq 
			if(IGNORE_VO_GEN.contains(file.getName())) continue;
			
			ProModule module = file.getModule();
			String fileGenPath = (localPath + "/" + artifactId + "/"+artifactId+"_main/src/main/java/" + groupId + "." + artifactId).replaceAll("\\.", "/");
			String filePkg = groupId + "." + artifactId;
			
			String fileClassPath = filePkg + ".vo.gen.Super" + file.getName();
			String fileTargetPath = fileGenPath + "/vo/gen/Super" + file.getName() + ".java";
			
			String childClassPath = filePkg + ".vo." + file.getName();
			String childTargetPath = fileGenPath + "/vo/" + file.getName() + ".java";
			
			if( ProjectModulTypeEnum.module.name().equals(packageType) ) {
				if(null != module && StringUtils.isNotBlank(module.getName())) { // 模块化
					fileTargetPath = fileGenPath + "/" + module.getName() + "/vo/gen/Super" + file.getName() + ".java";
					fileClassPath = filePkg + "." + module.getName() + ".vo.gen.Super" + file.getName();
					
					childTargetPath = fileGenPath + "/" + module.getName() + "/vo/" + file.getName() + ".java";
					childClassPath = filePkg + "." + module.getName() + ".vo." + file.getName();
				}
			}
			
			/**
			 * 范型
			 */
			if("yes".equalsIgnoreCase(file.getParadigm())) {
				fileClassPath += "<T>";
			}
			
			FullyQualifiedJavaType fileType = new FullyQualifiedJavaType(fileClassPath);
			TopLevelClass topClass = new TopLevelClass(fileType);
			topClass.setVisibility(JavaVisibility.PUBLIC);
			if(StringUtils.isNotBlank(file.getComment())) {
				topClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
			}
			/**
			 * 导入vo import
			 */
			for(String importedType : file.getImportTypes()) {
				topClass.addImportedType(importedType);
			}
			
			/**
			 * set super class
			 */
			if(StringUtils.isNotBlank(file.getSuperName())) {
				String superClass = StringUtil.getJavaTableName(file.getSuperName());
				topClass.addImportedType(filePkg + ".domain." +superClass);
				topClass.setSuperClass(superClass);
			}
			
			/**
			 * validate inner class
			 */
			Set<String> innerValidClass = new HashSet<>();
			
			/**
			 * vo生成
			 */
			List<ProField> fields = file.getFields();
			for(ProField field: fields) {
				
				Field f = new Field();
				f.setVisibility(JavaVisibility.PRIVATE);
				f.setName(field.getName());
				
				if(StringUtils.isNotEmpty(field.getComment())) {
					f.addJavaDocLine("/**" +field.getComment() + " */");
				}
				
				// DataType is Date import java.util.date
				if("Date".equals(field.getTypePath())) {
					topClass.addImportedType("java.util.Date");
				}
				
				String typeStr = CodeUtils.typeByCollectionType(field.getTypePath(), field.getCollectionType());
				
				// set valid
				Map<String, Set<String>> valids = file.getPropertyValid().get(field.getName());
				
				if(null != valids) {
					Set<String> keys = valids.keySet();
					for(String v: keys) {
						
						Set<String> groupSet = valids.get(v);
						innerValidClass.addAll(groupSet);
						
						// annotiation group
						String annotation = getFieldValidAnnotation(v, groupSet);
						f.addAnnotation(annotation);
						
						// gen group interface
						
						// import valid class
						String vName = v.substring(0, v.indexOf("("));
						String vCPath = commValidMap.get(vName);
						topClass.addImportedType(vCPath);
					}
				}
				
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(typeStr);
				f.setType(type);
				topClass.addField(f);
				
				
				/**
				 * 添加 set get 方法
				 */
				Method getM = new Method("get" + StringUtil.firstUpCase(field.getName()));
				getM.setVisibility(JavaVisibility.PUBLIC);
				getM.setReturnType(type);
				getM.addBodyLine("return this." + field.getName() + ";");
				topClass.addMethod(getM);
				
				// set
				Method setM = new Method("set" + StringUtil.firstUpCase(field.getName()));
				setM.setVisibility(JavaVisibility.PUBLIC);
				setM.addBodyLine("this." + field.getName() + " = "+field.getName()+";");
				Parameter parameter = new Parameter(type, field.getName());
				setM.addParameter(parameter);
				topClass.addMethod(setM);
			}
			
			/**
			 * 设置get 方法使用于 table的验证使用
			 **/
			List<ProField> validateMethods = file.getValidateMethods();
			for(ProField vm:validateMethods) {
				Map<String, Set<String>> valids = file.getPropertyValid().get(vm.getName());
				if(null != valids) {
					
					Method getM = new Method("get" + StringUtil.getJavaTableName(vm.getName()));
					getM.setVisibility(JavaVisibility.PUBLIC);
					FullyQualifiedJavaType type = new FullyQualifiedJavaType(vm.getDataType());
					getM.setReturnType(type);
					getM.addBodyLine("return super.get" + StringUtil.getJavaTableName(vm.getName()) + "();");
					
					getM.addAnnotation("@Override");
				
					// import type
					if(vm.getDataType().indexOf(".") > -1) {
						topClass.addImportedType(vm.getDataType());
					}
					
					Set<String> keys = valids.keySet();
					for(String v: keys) {
						
						Set<String> groupSet = valids.get(v);
						innerValidClass.addAll(groupSet);
						
						// annotiation group
						String annotation = getFieldValidAnnotation(v, groupSet);
						getM.addAnnotation(annotation);
						
						// gen group interface
						
						// import valid class
						String vName = v.substring(0, v.indexOf("("));
						String vCPath = commValidMap.get(vName);
						topClass.addImportedType(vCPath);
					}
					topClass.addMethod(getM);
				}
			}
			
			File genFile = new File(fileTargetPath);
			if( ! genFile.getParentFile().exists()) {
				new File(fileTargetPath).getParentFile().mkdirs();
			}
			
			// inner valid class - innerValidClass
			String inStr = getValidInnerClass(innerValidClass);
			String clStr = topClass.getFormattedContent();
			clStr = clStr.substring(0, clStr.lastIndexOf("}")) + NEW_LINE + inStr + NEW_LINE+"}";
			
			IOUtils.write(clStr, new FileOutputStream(new File(fileTargetPath)), "utf-8");
			
			File childClass = new File(childTargetPath);
			if( ! childClass.exists()) {
				// inner extend
				String t = "";
				if("yes".equalsIgnoreCase(file.getParadigm())) {
					t += "<T>";
				}
				
				// 
				FullyQualifiedJavaType childType = new FullyQualifiedJavaType(childClassPath + t);
				
				TopLevelClass childTypeClass = new TopLevelClass(childType);
				childTypeClass.addImportedType(fileClassPath);
				childTypeClass.addImportedType(JsonInclude.class.getName());
				
				childTypeClass.addAnnotation("@JsonInclude(JsonInclude.Include.NON_NULL)");
				
				// 判断是否为T
				childTypeClass.setSuperClass("Super" + file.getName() + t);
				
				childTypeClass.setVisibility(JavaVisibility.PUBLIC);
				childTypeClass.addFileCommentLine("/** "+NEW_LINE + file.getComment() + NEW_LINE+" **/");
				IOUtils.write(childTypeClass.getFormattedContent(), new FileOutputStream(childClass), "utf-8");
			}
		}
	}

	private String getValidInnerClass(Set<String> innerValidClass) {
		StringBuilder ins = new StringBuilder();
		for(String iC: innerValidClass) {
			ins.append(String.format("\tpublic interface %s{} "+NEW_LINE, iC));
		}
		return ins.toString();
	}
	
	private String getFieldValidAnnotation(String annotation, Set<String> groupSet) {
		Set<String> gs = new HashSet<>();
		for(String g: groupSet) {
			gs.add(g+".class");
		}
		
		boolean hasValue = ! annotation.endsWith("()");
		StringBuilder sb = new StringBuilder("@");
		sb.append(annotation.substring(0, annotation.indexOf("(")+1));
		sb.append("groups={");
		sb.append(Strings.join(gs, ','));
		sb.append("}");
		if(hasValue) sb.append(", ");
		sb.append(annotation.substring(annotation.indexOf("(")+1, annotation.length()));
		return sb.toString();
	}
	
	public FileIdsAndType getFieldTypes(List<ProField> fields) {
		FileIdsAndType fit = new FileIdsAndType();
		for(ProField field : fields) {
			if(FieldDataType.vo.name().equalsIgnoreCase(field.getDataType())) {
				fit.getTypeIds().add(Long.valueOf(field.getTypeKey()));
			}
			String type = getCollectionClassPathByName(field.getCollectionType());
			if(type != null) fit.getTypePaths().add(type);
		}
		return fit;
	}
	
}
