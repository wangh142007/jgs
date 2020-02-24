package com.wh.controller;

import com.wh.pojo.UserAddress;
import com.wh.pojo.bo.AddressBO;
import com.wh.pojo.bo.SubmitOrderBO;
import com.wh.service.AddressService;
import com.wh.utils.IMOOCJSONResult;
import com.wh.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: latte
 * @description:
 * @author: wh
 * @create: 2020-01-14 17:51
 */

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orders")
@Api(value = "订单相关", tags = {"订单相关的接口"})
public class OrdersController {

    private AddressService addressService;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO) {
        //1.创建订单
        //2.创建订单以后，移除购物车中已经结算的商品
        //3.向支付中心发送当前订单，用于保存支付中心的订单数据

        return IMOOCJSONResult.ok();
    }


}
