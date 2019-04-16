package com.cd2cd.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cd2cd.comm.ServiceCode;
import com.cd2cd.util.JWTHelperUtil;

/**
 * api 认证/授权验证
 */

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	
	@Resource
	private JWTHelperUtil jWTHelperUtil;
	
	@Resource
	private MyUserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		
		String header = request.getHeader("Authorization");
		
		LOG.info("header=" + header);
		
		boolean toAuthentication = false;
		if (header != null && header.startsWith("Bearer ")) {
			toAuthentication = true;
		} else {
			String token = request.getParameter("token");
			if(StringUtils.isNotEmpty(token)) {
				toAuthentication = true;
			}
		}
		
        if (toAuthentication) {
        	authentication(request, response);
        }
        
        chain.doFilter(request, response);
	}
	
	private void authentication(HttpServletRequest request, HttpServletResponse response) {
		
        String authHeader = request.getHeader("Authorization");
        String token = request.getParameter("token");
        
        LOG.info("authHeader={}, token={}", authHeader, token);
        String tokenHead = "Bearer ";
        if(StringUtils.isBlank(authHeader) && StringUtils.isBlank(token)) {
        	return;
        } else if(StringUtils.isBlank(authHeader)) {
        	authHeader = token;
        	tokenHead = "";
        }
        
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
        	String authToken = authHeader.substring(tokenHead.length());
        	
        	try {
	        	LoginUser mUserVo = jWTHelperUtil.verifyToken(authToken);
	        	if (mUserVo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        		
	        		LOG.info("username={}", mUserVo.getUsername());
	        		
	        		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(mUserVo, null, mUserVo.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	        	}
	            
        	} catch(Exception e) {
        		e.printStackTrace();
        		JSONObject jsonObj = new JSONObject();
    			try {
					jsonObj.put("code", ServiceCode.TOKEN_INVALID.code);
	    			jsonObj.put("msg", ServiceCode.TOKEN_INVALID.msg);
	        		response.setContentType("text/json");
	        		response.getWriter().println(jsonObj.toString());
    			} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        }
    }
}
