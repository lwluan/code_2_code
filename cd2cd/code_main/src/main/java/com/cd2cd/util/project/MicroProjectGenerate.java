package com.cd2cd.util.project;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MicroProjectGenerate extends ProjectGenerate {


    @Override
    protected void genProjectBase() {

    }

    @Override
    protected void genDomain() {

    }

    @Override
    protected void genControllerAndService() {

    }

    @Override
    protected void getVo() {

    }
}
