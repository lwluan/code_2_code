package com.boyu.multisource;


public class TenantThreadLocal {

	private static InheritableThreadLocal<HolderBean> idLocal;
	public static String defaulTenantId = "databaseName"; // map

	static {
		idLocal = new InheritableThreadLocal<>();
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
		HolderBean hb = getHolderBean();
		hb.setT(t);
		setHolderBean(hb);
	}
	
	public static String getAppId() {
		return getHolderBean().getA();
	}

	public static void setAppId(String a) {
		getHolderBean().setA(a);
	}
	
	public static void removeId() {
		idLocal.remove();
	}
}
