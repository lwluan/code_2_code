package com.boyu.code_microservice_webui.controller;

import com.boyu.e_commerce_common.code.ServiceCode;
import com.boyu.e_commerce_goods.GoodsClient;
import com.boyu.e_commerce_repository.vo.BaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsClient goodsClient;

    @PostMapping("goodsList")
    public BaseRes<String> goodsList() {
        return goodsClient.goodsList();
    }
}
