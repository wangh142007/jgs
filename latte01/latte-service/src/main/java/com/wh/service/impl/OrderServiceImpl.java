package com.wh.service.impl;

import com.wh.enmus.OrderStatusEnum;
import com.wh.enmus.YesOrNo;
import com.wh.mapper.OrderItemsMapper;
import com.wh.mapper.OrderStatusMapper;
import com.wh.mapper.OrdersMapper;
import com.wh.org.n3r.idworker.Sid;
import com.wh.pojo.*;
import com.wh.pojo.bo.SubmitOrderBO;
import com.wh.service.AddressService;
import com.wh.service.ItemService;
import com.wh.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 *
 * @author wh
 * @create 2020/2/25 0025
 * @since 1.0.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersMapper ordersMapper;
    private final OrderItemsMapper orderItemsMapper;
    private final OrderStatusMapper orderStatusMapper;
    private final AddressService addressService;
    private final ItemService itemService;
    private final Sid sid;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        Integer payMethod = submitOrderBO.getPayMethod();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        String userId = submitOrderBO.getUserId();
        //包邮费用设置为0
        Integer postAmount = 0;
        //1.新订单数据保存
        String orderId = sid.nextShort();
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        UserAddress userAddress = addressService.queryUserUserAddress(userId, addressId);
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(userAddress.getProvince() + " " + userAddress.getCity() + " "
                + userAddress.getDistrict() + " " + userAddress.getDetail());

        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        //2.虚幻根据itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        //商品原价累计
        Integer totalAmount = 0;
        //优惠后的实际支付价格累计
        Integer realAmount = 0;
        for (String itemSpecId : itemSpecIdArr) {
            //TODO 整合redis后，商品购买的数量重新从redis的购物车获取
            int buyCounts = 1;
            //2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemsSpec = itemService.queryItemsSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realAmount += itemsSpec.getPriceDiscount() * buyCounts;
            //2.2 根据商品id，获取商品信息以及商品图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);
            //2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemName(items.getItemName());
            subOrderItems.setItemImg(imgUrl);
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(itemSpecId);
            subOrderItems.setItemSpecName(itemsSpec.getName());
            subOrderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItems);
            //2.4 扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realAmount);
        ordersMapper.insert(newOrder);
        //3.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);
        return orderId;
    }
}
