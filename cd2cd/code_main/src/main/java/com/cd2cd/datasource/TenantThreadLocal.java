package com.cd2cd.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TenantThreadLocal {

	private Logger log = LoggerFactory.getLogger(TenantThreadLocal.class);

	private static InheritableThreadLocal<HolderBean> idLocal;
	protected static String defaulTenantId = "auto_code";
	static {
		idLocal = new InheritableThreadLocal<>();
	}

	@Value("${spring.multisource.url}")
	private String multisourceUrl;

	@PostConstruct
	protected void initTenantThreadLocal() {
		int i = multisourceUrl.indexOf("{")+1;
		int e = multisourceUrl.indexOf("}");
		defaulTenantId = multisourceUrl.substring(i, e);
		log.info("defaulTenantId={}", defaulTenantId);
	}

	public static void setHolderBean(HolderBean holder) {
		idLocal.set(holder);
	}
	private static HolderBean getHolderBean() {
		HolderBean holder = idLocal.get();
		if(holder == null) {
			holder = new HolderBean();
			idLocal.set(new HolderBean());
		}
		return holder;
	}
	
	public static String getTenantId() {
		String id = getHolderBean().getT();
		if (id == null)
			id = defaulTenantId;
		return id;
	}

	public static void setTenantId(String t) {
		getHolderBean().setT(t);
	}
	
	public static String getAppId() {
		return getHolderBean().getA();
	}

	public static void setAppId(String a) {
		getHolderBean().setA(a);
	}
	
	public static String getChannelId() {
		return getHolderBean().getC();
	}

	public static void setChannelId(String c) {
		getHolderBean().setC(c);
	}
	
	public static void removeId() {
		idLocal.remove();
	}
}
