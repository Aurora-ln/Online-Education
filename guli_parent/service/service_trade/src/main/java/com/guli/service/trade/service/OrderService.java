package com.guli.service.trade.service;

import com.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author Aurora
 * @since 2020-06-26
 */
public interface OrderService extends IService<Order> {

    String saveOrder(String courseId, String id);

    Order getByOrderId(String orderId, String id);

    Boolean isBuyByCourseId(String courseId, String memberId);

    List<Order> selectByMemberId(String memberId);

    boolean removeById(String orderId, String  memberId);

    Order getOrderByOrderNo(String orderNo);

    void updateOrderStatus(Map<String, String> notifyMap);

    boolean queryPayStatus(String orderNo);
}
