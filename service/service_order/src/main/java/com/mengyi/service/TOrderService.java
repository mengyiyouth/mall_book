package com.mengyi.service;

import com.mengyi.entity.OrderGoods;
import com.mengyi.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengyi.vo.AllOrdersAndGoods;
import com.mengyi.vo.OrderQuery;

import java.util.List;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-14
 */
public interface TOrderService extends IService<TOrder> {

    String createOrder(String[] ids, String[] counts, String memberIdByJwtToken);

    List<OrderGoods> getOrders(String orderId, OrderQuery orderQuery);

    List<AllOrdersAndGoods> getAllOrder(String memberId, OrderQuery orderQuery);

    void rollBackBuyCountAndReverse(TOrder order);

    Integer countSaleDay(String day);
}
