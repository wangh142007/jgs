package com.wh.controller;

import com.wh.enmus.PayMethod;
import com.wh.pojo.UserAddress;
import com.wh.pojo.bo.AddressBO;
import com.wh.pojo.bo.SubmitOrderBO;
import com.wh.service.AddressService;
import com.wh.service.OrderService;
import com.wh.utils.CookieUtils;
import com.wh.utils.IMOOCJSONResult;
import com.wh.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Queue;

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
public class OrdersController extends BaseController {

    private OrderService orderService;

    @ApiOperation(value = "创建订单", notes = "创建订单", httpMethod = "POST")
    @PostMapping("/create")
    public IMOOCJSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //1.创建订单

        if (!submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type) &&
                !submitOrderBO.getPayMethod().equals(PayMethod.ALIPAY.type)
        ) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }
        String orderId = orderService.createOrder(submitOrderBO);
        //2.创建订单以后，移除购物车中已经结算的商品
        //TODO 整合redis之后，完善购物车中已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        //3.向支付中心发送当前订单，用于保存支付中心的订单数据

        return IMOOCJSONResult.ok(orderId);
    }


}
