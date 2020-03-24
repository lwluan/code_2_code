package com.cd2cd.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
 
public class ResourcesCopyUtil {
	private static final Logger log = LoggerFactory.getLogger(ResourcesCopyUtil.class);

	public static void loadRecourseFromJarByFolder(String folderPath, String targetFolderPath, Class clazz) throws IOException {
		URL url = clazz.getResource(folderPath);
		URLConnection urlConnection = url.openConnection();

		boolean isFileUrl = urlConnection instanceof FileURLConnection;
		log.info("FileURLConnection={}", isFileUrl);
		if (isFileUrl) {
			copyFileResources(url, folderPath, targetFolderPath, clazz);
		} else if (urlConnection instanceof JarURLConnection) {
			copyJarResources((JarURLConnection) urlConnection, folderPath, targetFolderPath, clazz);
		}
	}

	/**
	 * 当前运行环境资源文件是在文件里面的
	 *
	 * @param url
	 * @param folderPath
	 * @param clazz
	 * @throws IOException
	 */
	private static void copyFileResources(URL url, String folderPath, String targetFolderPath, Class clazz) throws IOException {
		File root = new File(url.getPath());
		if (root.isDirectory()) {
			File[] files = root.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					loadRecourseFromJarByFolder(folderPath + "/" + file.getName(), targetFolderPath, clazz);
				} else {
					loadRecourseFromJar(folderPath + "/" + file.getName(), targetFolderPath, clazz);
				}
			}
		}
	}

	/**
	 * 当前运行环境资源文件是在jar里面的
	 *
	 * @param jarURLConnection
	 * @throws IOException
	 */
	private static void copyJarResources(JarURLConnection jarURLConnection, String folderPath, String targetFolderPath, Class clazz) throws IOException {
		JarFile jarFile = jarURLConnection.getJarFile();
		Enumeration<JarEntry> entrys = jarFile.entries();
		while (entrys.hasMoreElements()) {
			JarEntry entry = entrys.nextElement();
			if (entry.getName().startsWith(jarURLConnection.getEntryName()) && !entry.getName().endsWith("/")) {
				loadRecourseFromJar("/" + entry.getName(), targetFolderPath, clazz);
			}
		}
		jarFile.close();
	}

	public static void loadRecourseFromJar(String path, String recourseFolder, Class clazz) throws IOException {

		if (!path.startsWith("/")) {
			throw new IllegalArgumentException("The path has to be absolute (start with '/').");
		}

		if (path.endsWith("/")) {
			throw new IllegalArgumentException("The path has to be absolute (cat not end with '/').");
		}

		int index = path.lastIndexOf('/');

		String filename = path.substring(index + 1);
		String folderPath = recourseFolder + path.substring(0, index + 1);

		// If the folder does not exist yet, it will be created. If the folder
		// exists already, it will be ignored
		File dir = new File(folderPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// If the file does not exist yet, it will be created. If the file
		// exists already, it will be ignored
		filename = folderPath + filename;
		File file = new File(filename);

		if (!file.exists() && !file.createNewFile()) {
			log.error("create file :{} failed .fileName:" + filename);
			return;
		}

		// Prepare buffer for data copying
		byte[] buffer = new byte[1024];
		int readBytes;

		// Open and check input stream
		URL url = clazz.getResource(path);
		URLConnection urlConnection = url.openConnection();
		InputStream is = urlConnection.getInputStream();

		if (is == null) {
			throw new FileNotFoundException("File " + path + " was not found inside JAR.");
		}
		OutputStream os = new FileOutputStream(file);
		try {
			while ((readBytes = is.read(buffer)) != -1) {
				os.write(buffer, 0, readBytes);
			}
		} finally {
			os.close();
			is.close();
		}
	}

	public static void main(String[] args) throws IOException {
		loadRecourseFromJarByFolder("/code-microservice", "/Users/lwl/Desktop", ResourcesCopyUtil.class);

	}
}