package com.cd2cd.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

/**
 * 前端模板使用
 */
@Controller
public class HtmlController {

	public String file(HttpServletRequest request) {
		String path = request.getServletPath();
		path = path.substring(0, path.lastIndexOf(".html"));
		System.out.println("path=" + path);
		return path;
	}
	
}
