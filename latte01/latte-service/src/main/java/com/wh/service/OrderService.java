package com.wh.service;

import com.wh.pojo.Carousel;
import com.wh.pojo.bo.SubmitOrderBO;

import java.util.List;

/**
 * @program: latte
 * @description:
 * @author: wh
 * @create: 2020-01-21 18:11
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param submitOrderBO
     */
    String createOrder(SubmitOrderBO submitOrderBO);

}
