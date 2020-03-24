package com.cd2cd.domain;

import com.cd2cd.domain.gen.SuperProModule;
import java.io.Serializable;

public class ProModule extends SuperProModule implements Serializable {
    private static final long serialVersionUID = 1L;

    private String microName;

    public String getMicroName() {
        return microName;
    }

    public void setMicroName(String microName) {
        this.microName = microName;
    }
}