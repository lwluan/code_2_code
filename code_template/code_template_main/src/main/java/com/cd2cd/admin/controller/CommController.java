package com.cd2cd.admin.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.security.LoginUser;
import com.cd2cd.admin.security.Md5PasswordEncoder;
import com.cd2cd.admin.security.MyUserDetailsService;
import com.cd2cd.admin.service.SysAuthorityService;
import com.cd2cd.admin.util.JWTHelperUtil;
import com.cd2cd.admin.vo.BaseRes;

@RestController
@RequestMapping("comm")
public class CommController {

	@Resource
	private SysAuthorityService sysAuthorityService;
	
	@Resource
	private MyUserDetailsService myUserDetailsService;
	
	@Resource
	private JWTHelperUtil jWTHelperUtil;
	
	@RequestMapping(value = "admin/login", method = RequestMethod.POST)
	public BaseRes<String> userLogin(String username, String password) {
		BaseRes<String> res = new BaseRes<String>();
		
		LoginUser loginUser = myUserDetailsService.loadUserByUsername(username);
		password = Md5PasswordEncoder.md5Encode(password);
		if( loginUser.getPassword().equals(password) ) {
			String token = jWTHelperUtil.getToken(loginUser);
			res.setData(token);
			res.setServiceCode(ServiceCode.SUCCESS);
		} else {
			res.setServiceCode(ServiceCode.LOGIN_ERROR);
		}
		
		return res;
	}
}