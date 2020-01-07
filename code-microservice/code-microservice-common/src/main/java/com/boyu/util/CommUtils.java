package com.boyu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class CommUtils {

	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);
	private static final Logger log = LoggerFactory.getLogger(CommUtils.class);
	private static AtomicInteger atomicInteger = new AtomicInteger(0);

	public static String randomUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String genTokenKey(String tenant, String type, String username) {
		return String.format("%s-%s-%s", tenant, type, username);
	}

	public static Map<String, String> genMap(String ...args) {
		Map<String, String> map = new HashMap<>();
		for(int i=0; i<args.length; i+=2) {
			map.put(args[i], args[i+1]);			
		}
		return map;
	}
	
	public static String serialId(String prefix) {
		// 32位
		String dStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()); // 17
		String uuid = prefix + randomUUID(); // 18
		return uuid.substring(0, 15) + dStr;
	}

	/**生成订单号 年月日时分+6位递增数字*/
	public static String orderNumber(){
		String date = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
		String format = String.format("%06d", atomicInteger.intValue());
		return date+format;
	}

	public static String getClientId() {
		HttpServletRequest request = getRequest();

		String ip = request.getHeader("X-Real-IP");
		if (ip!= null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (ip!= null && !"".equals(ip)  && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			System.out.println(ip);
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

//	public static LoginUser getLoginUserInfo() {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Authentication authentication = securityContext.getAuthentication();
//		return (LoginUser) authentication.getPrincipal();
//	}

	public static Date getBirthdayByIdCard(String idCard) {
		try {
			String birStr = idCard.substring(6, 14);
			log.info("birStr={}", birStr);
			return new SimpleDateFormat("yyyyMMdd").parse(birStr);
		} catch (ParseException e) {
			log.error("idCard={}", idCard, e);
		}
		return null;
	}

	public static Date getDataByStr(String date, boolean e) {

		String[] ds = date.split("-");
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set(Calendar.YEAR, Integer.valueOf(ds[0]));
		mCalendar.set(Calendar.MONTH, Integer.valueOf(ds[1])-1);
		mCalendar.set(Calendar.DATE, Integer.valueOf(ds[2]));

		if(e) {
			mCalendar.set(Calendar.HOUR_OF_DAY, 23);
			mCalendar.set(Calendar.MINUTE, 59);
			mCalendar.set(Calendar.SECOND, 59);
		} else {
			mCalendar.set(Calendar.HOUR_OF_DAY, 0);
			mCalendar.set(Calendar.MINUTE, 0);
			mCalendar.set(Calendar.SECOND, 0);
		}
		return mCalendar.getTime();
	}

	// 性别 Wanita：0（女性 ），Pria ：1（男性）
	public static Byte getGender(String gender) {
		return Byte.valueOf("男".equals(gender) ? "0" : "1");
	}

	public static String AES_CBC_Encrypt(String content, byte[] keyBytes, byte[] iv) {
		try {
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(content.getBytes());
			return new String(Base64.encodeBase64(result), "UTF-8");
		} catch (Exception e) {
			System.out.println("exception:" + e.toString());
		}
		return null;
	}

	public static String AES_CBC_Decrypt(String content, byte[] keyBytes, byte[] iv) {

		try {
			content = content.replaceAll(" ", "+");
			byte[] decryptBaseData = Base64.decodeBase64(content.getBytes("utf-8"));
			SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] result = cipher.doFinal(decryptBaseData);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String randomSmsValidCode() {
		Random rand = new Random();
		StringBuilder s = new StringBuilder(6);
		for (int i = 0; i < 6; i++) {
			s.append((int) (rand.nextInt(9)));
		}
		return s.toString();
	}

	public static String md5(String content) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(content.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("NoSuchAlgorithmException", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10) {
				hex.append(0);
			}
			hex.append(Integer.toHexString(b & 0xff));
		}

		return hex.toString();
	}
}
