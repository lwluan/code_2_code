package com.cd2cd.admin.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CommUtils {

	public static String randomUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String serialId(String prefix) {
		// 32位
		String dStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // 17
		String uuid = prefix + randomUUID(); // 18
		return uuid.substring(0, 15) + dStr;
	}
	
	public static String getClientIp() {
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String md5Encode(String password) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
	
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	public static HttpServletResponse getResponse() {
        HttpServletResponse response=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
	}
	
	public static String randomSmsValidCode() {
		Random rand = new Random();
		StringBuilder s = new StringBuilder(6);
		for (int i = 0; i < 6; i++) {
			s.append((int) (rand.nextInt(9)));
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(md5Encode("admin111111"));
	}
}
