package com.cd2cd.util;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cd2cd.comm.ServiceCode;
import com.cd2cd.comm.exceptions.ServiceBusinessException;
import com.cd2cd.security.LoginUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTHelperUtil {
	private static final Logger LOG = LoggerFactory.getLogger(JWTHelperUtil.class);
	
	private String JWT_SECRET = "wx_app_api_099888";
	private int expires_secs = 60 * 60 * 24 * 7; // 7天有效期
	private static final String USER_INFO_KEY = "userInfo";
	
	/**
	 * 生成token 设置： # 过期时间 # 用户标识 #
	 * 
	 * @param userVo
	 * @return
	 */
	public String getToken(LoginUser userVo) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);

			Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.SECOND, expires_secs);
			String userStr = new ObjectMapper().writeValueAsString(userVo);
			
			LOG.info("userStr={}", userStr);
			
			String token = JWT.create()
					.withClaim(USER_INFO_KEY, userStr)
					.withExpiresAt(expiresAt.getTime())
					.sign(algorithm);
			
			LOG.info("userStr={}, token={}", userStr, token);
			
			return token;
		} catch (JWTVerificationException exception) {
			throw new ServiceBusinessException(ServiceCode.GEN_JWT_TOKEN_FAIL, exception.getMessage());
		} catch (JsonProcessingException e) {
			throw new ServiceBusinessException(ServiceCode.GEN_JWT_TOKEN_FAIL, e.getMessage());
		} 
	}

	public LoginUser verifyToken(String token) {
		try {
			
			LOG.info("token={}", token);
			
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable
																						// verifier
			DecodedJWT jwt = verifier.verify(token);
			String userStr = jwt.getClaim(USER_INFO_KEY).asString();
 
			
			LOG.info("userStr={}, token={}, ExpiresAt={}", userStr, token, jwt.getExpiresAt());
			
			LoginUser userInfo = JSONObject.parseObject(userStr, LoginUser.class);
			return userInfo;
		} catch (JWTVerificationException exception) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, exception.getMessage());
		} catch (Exception e) {
			throw new ServiceBusinessException(ServiceCode.TOKEN_INVALID, e.getMessage());
		}
	}

}
