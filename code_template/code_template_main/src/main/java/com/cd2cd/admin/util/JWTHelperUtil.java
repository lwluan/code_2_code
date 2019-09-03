package com.cd2cd.admin.util;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cd2cd.admin.comm.ServiceCode;
import com.cd2cd.admin.comm.exceptions.ServiceBusinessException;
import com.cd2cd.admin.dao.cache.DataCacheDao;
import com.cd2cd.admin.security.LoginUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTHelperUtil {

	private String JWT_SECRET = "wx_app_api_099888";
	private int expires_secs = 60 * 60 * 24 * 7; // 7天有效期
	private static final String USER_INFO_KEY = "userInfo";

	@Resource
	private DataCacheDao dataCacheDao;

	/**
	 * 生成token 设置： # 过期时间 # 用户标识 #
	 * 
	 * @param userVo
	 * @return
	 */
	public String getToken(Object loginObj) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

			Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.SECOND, expires_secs);
			String userStr = new ObjectMapper().writeValueAsString(loginObj);

			String token = JWT.create().withClaim(USER_INFO_KEY, userStr).withExpiresAt(expiresAt.getTime()).sign(algorithm);
			return token;
		} catch (JWTVerificationException exception) {
			throw new ServiceBusinessException(ServiceCode.GEN_JWT_TOKEN_FAIL, exception.getMessage());
		} catch (JsonProcessingException e) {
			throw new ServiceBusinessException(ServiceCode.GEN_JWT_TOKEN_FAIL, e.getMessage());
		}
	}

	public String getClaimByToken(String token) {
		Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
		JWTVerifier verifier = JWT.require(algorithm).build(); 
																
		DecodedJWT jwt = verifier.verify(token);
		String userStr = jwt.getClaim(USER_INFO_KEY).asString();
		return userStr;
	}

	public LoginUser verifyAdminToken(String token) {

		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build(); 
			verifier.verify(token);
			LoginUser adminUser = dataCacheDao.getAdminLoginCacheData(token);

			return adminUser;
		} catch (JWTVerificationException exception) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, exception.getMessage());
		} catch (Exception e) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, e.getMessage());
		}
	}
	
	public LoginUser verifyApiToken(String token) {

		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build(); 
			verifier.verify(token);
			LoginUser adminUser = dataCacheDao.getApiLoginCacheData(token);

			return adminUser;
		} catch (JWTVerificationException exception) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, exception.getMessage());
		} catch (Exception e) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, e.getMessage());
		}
	}

}
