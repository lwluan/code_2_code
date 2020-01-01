package com.boyu.code_microservice_repository.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantThreadLocal {
    private static InheritableThreadLocal<HolderBean> idLocal;

    static {
        idLocal = new InheritableThreadLocal<>();
    }

    public static void setHolderBean(HolderBean holder) {
        idLocal.set(holder);
    }

    private static HolderBean getHolderBean() {
        HolderBean holder = idLocal.get();
        if (holder == null) {
            holder = new HolderBean();
            idLocal.set(new HolderBean());
        }
        return holder;
    }

    public static String getTenantId() {
        String id = getHolderBean().getT();
        if (id == null)
            id = MultiDataSource.defaulTenantId;
        return id;
    }

    public static void setTenantId(String t) {
        getHolderBean().setT(t);
    }

    public static void removeId() {
        idLocal.remove();
    }

}
