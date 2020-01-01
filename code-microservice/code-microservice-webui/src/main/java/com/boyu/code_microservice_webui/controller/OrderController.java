package com.boyu.code_microservice_webui.controller;

import com.alibaba.fastjson.JSONObject;
import com.boyu.e_commerce_order.OrderClient;
import com.boyu.e_commerce_repository.vo.BaseRes;
import com.boyu.e_commerce_repository.vo.DataPageWrapper;
import com.boyu.e_commerce_repository.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    private Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderClient orderClient;

    @PostMapping("orderList")
    public BaseRes<DataPageWrapper<OrderVo>> orderList(@RequestBody OrderVo orderVo) {
        BaseRes<DataPageWrapper<OrderVo>> res = orderClient.orderList(orderVo);

        log.info("res={}", JSONObject.toJSONString(res));

        return res;
    }

}
