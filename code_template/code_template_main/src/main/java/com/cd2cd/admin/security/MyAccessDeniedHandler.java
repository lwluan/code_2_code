package com.cd2cd.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.cd2cd.admin.comm.ServiceCode;

public class MyAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse,
			AccessDeniedException paramAccessDeniedException)
			throws IOException, ServletException {

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("code", ServiceCode.ACCESS_DENIED.code);
			jsonObj.put("msg", ServiceCode.ACCESS_DENIED.msg);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		paramHttpServletResponse.setContentType("text/json");
		paramHttpServletResponse.getWriter().println(jsonObj.toString());

	}

}
