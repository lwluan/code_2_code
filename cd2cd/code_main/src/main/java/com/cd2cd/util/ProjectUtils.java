package com.cd2cd.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cd2cd.domain.ProProject;

public class ProjectUtils {
	private static String code_path = "code_template";
	
	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 */
	public static void copyFolder(File src, File dest) throws IOException {
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

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}
	
	private static String fileFolderPath(String name) {
		String _path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "../../"+name+"/";
		_path = _path.replace("file:", "");
		
		File file = new File(_path);
		if( ! file.exists() ) {
			file.mkdirs();
		}
		_path = getRealPath(_path);
		return _path;
	}
	
	private static String getRealPath(String path) {
		int n = path.indexOf("../");
		if( n > -1 ) {
			String eStr = path.substring(n + 3, path.length());
			String sStr = path.substring(0, n -1);
			sStr = sStr.substring(0, sStr.lastIndexOf("/") + 1);
			String p = sStr + eStr;
			path = p;
			if( path.indexOf("../") > -1 ) {
				path = getRealPath(path);
			}
		}
		return path;
	}
	
	public static void genProject(ProProject project) throws Exception {
		String contextPath = project.getContextPath();
		String artifactId = project.getArtifactId();
		String groupId = project.getGroupId();
		String name = project.getName();
		String packageType = project.getPackageType();
		String version = project.getVersion();
		String description = project.getDescription();
		String localPath = project.getLocalPath();
		
		if( ! new File(localPath).exists() ) {
			throw new Exception("localPath=" + localPath + ",不存在");
		}
		
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
		
		String _path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "../../../../" + code_path;
		_path = _path.replace("file:", "");
		String tpPath = getRealPath(_path);
		
		// 1、项目复制
		File src = new File(tpPath);
		File dest = new File(localPath + "/" + code_path);
		if( ! new File(localPath + "/" + artifactIdName).exists() ) {
			copyFolder(src, dest);
		}
		// 2、项目替换
		replaceProject(project);
	}
	
	/**
	 * 替换文件内容
	 * @param project
	 * @throws Exception
	 */
	public static void replaceProject(ProProject project) throws Exception {
		String contextPath = project.getContextPath();
		String artifactId = project.getArtifactId();
		String groupId = project.getGroupId();
		String name = project.getName();
		String packageType = project.getPackageType();
		String version = project.getVersion();
		String description = project.getDescription();
		String localPath = project.getLocalPath();
		
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
		String targetMainSrcGroupIdPath =  targetMainPath + "/src/main/java/" + groupIdName;
		
		// rename group id
		reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
		projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;
		
		// rename artifact id
		String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
		String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;
		
		reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
		projectMainSrcArtifactIdPath = targetMainSrcArtifactIdPaht;
		
		reNameFolder(project);
		replacePomXml(project);
		replaceJavaAndMapperXml(project);
	}
	
	private static void replaceJavaAndMapperXml(ProProject project) throws Exception {
		String contextPath = project.getContextPath();
		String artifactId = project.getArtifactId();
		String groupId = project.getGroupId();
		String localPath = project.getLocalPath();
		
		String groupIdName = groupId.replaceAll("\\.", "/").replaceAll("-", "_");
		String artifactIdName = artifactId.replaceAll("\\.", "/").replaceAll("-", "_");
		String projectPath = localPath + "/" + artifactIdName;
		
        List<File> files = new ArrayList<File>();
		listAllFile(projectPath, files);

		String packageSrc = "package com.cd2cd.admin";
		String packageDesc = "package " + groupId + "." + artifactIdName;
		String importSrc = "import com.cd2cd.admin.";
		String importDesc = "import " + groupId + "." + artifactIdName + ".";
		
		for( File f: files ) {
			
			if( f.getPath().endsWith(".java") ) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content
						.replaceAll(packageSrc, packageDesc)
						.replaceAll(importSrc, importDesc);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			} else if( f.getPath().endsWith(".properties") ) {
				
				Properties props = new Properties(); 
				props.load(new FileInputStream(f));
				props.setProperty("mybatis.mapper-locations", "classpath:" + groupIdName + "/" + artifactIdName + "/**/*Mapper.xml" );
				props.setProperty("mybatis.type-aliases-package", groupId + "." + artifactIdName + ".domain");
				props.setProperty("server.context-path", contextPath);
				
				props.store(new FileOutputStream(f), "test");
			} else if( f.getPath().endsWith("Mapper.xml") ) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content
						.replaceAll("com.cd2cd.admin", groupId + "." + artifactIdName);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			} else if( f.getPath().endsWith(".html") ) {
				String content = IOUtils.toString(new FileInputStream(f), "utf-8");
				content = content
						.replaceAll("/code_template", contextPath);
				IOUtils.write(content, new FileOutputStream(f), "utf-8");
			}
			
		}
	}

	private static void reNameFolder(ProProject project) {
		String artifactId = project.getArtifactId();
		String groupId = project.getGroupId();
		String localPath = project.getLocalPath();
		
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
		String targetMainSrcGroupIdPath =  targetMainPath + "/src/main/java/" + groupIdName;
		reNameFile(projectMainSrcGroupIdPath, targetMainSrcGroupIdPath);
		projectMainSrcGroupIdPath = targetMainSrcGroupIdPath;
		
		// rename artifact id
		String projectMainSrcArtifactIdPath = projectMainSrcGroupIdPath + "/admin";
		String targetMainSrcArtifactIdPaht = projectMainSrcGroupIdPath + "/" + artifactIdName;
		reNameFile(projectMainSrcArtifactIdPath, targetMainSrcArtifactIdPaht);
		projectMainSrcArtifactIdPath = targetMainSrcArtifactIdPaht;
		
		
	}
	
	private static void replacePomXml(ProProject project) throws Exception {
		
		// - = - = - = - = - 内容替换 - = - = - = - = -
		String artifactId = project.getArtifactId();
		String groupId = project.getGroupId();
		String version = project.getVersion();
		String localPath = project.getLocalPath();
		
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
		getChildsByTagName(propertiesNode, "start-class").get(0).setTextContent(groupId + "."+ artifactIdName +".AppMainStarter");
		
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
	
	private static void cleanNodeChilds(Node node) {
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			node.removeChild(nodeList.item(0));
			i--;
		}
	}
	
	
	private static List<Node> getChildsByTagName(Node node, String tagName) {
		NodeList nodeList = node.getChildNodes();
		List<Node> nodes = new ArrayList<Node>();
		for( int i=0; i<nodeList.getLength(); i++ ) {
			if( tagName.equals(nodeList.item(i).getNodeName()) ) {
				nodes.add(nodeList.item(i));
			}
		}
		return nodes;
	}
	private static void reNameFile(String f1, String f2) {
		File file1 = new File(f1);
		File file2 = new File(f2);
		
		if( file1.exists() && ! file2.exists() ) {
			file1.renameTo(file2);
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		ProProject project = new ProProject();
		project.setArtifactId("dome_admin");
		project.setGroupId("com.lwl");
		project.setContextPath("/demo_admin");
		project.setLocalPath("/Users/leiwuluan/Desktop/temp");
		project.setName("用户管理后台");
		project.setPackageType("standard");
		project.setVersion("1.0");
		project.setDescription("用户管理后台，人员管理");
		project.setUpdateTime(new Date());
		project.setCreateTime(new Date());
		
		genProject(project);
		
		// 修改 package
		// 修改 import
		
	}
	
	public static void listAllFile(String filePath, List<File> files) {  
        File file = new File(filePath);  
        
        File[] files2 = file.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if(file.isDirectory())  
		            return true;  
		        else {  
		            String name = file.getName();  
		            if(name.endsWith(".java") 
		            		|| name.endsWith(".xml") 
		            		|| name.endsWith(".html")
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
                if( fi.isFile() ) {
                	files.add(fi);  
                }
            }  
        }  
    }  
	
	public static Document getDocumentByFilePath(String filePath) throws Exception {
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
	
	public static void saveDocument(Document document, String filePath) throws Exception {
		String contentStr = toStringFromDoc(document);
		IOUtils.write(contentStr, new FileOutputStream(filePath), "utf-8");
	}
	
	public static String toStringFromDoc(Document document) {  
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
}
