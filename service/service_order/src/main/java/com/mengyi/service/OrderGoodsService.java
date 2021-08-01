package com.mengyi.service;

import com.mengyi.entity.OrderGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengyi.vo.OrderQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
public interface OrderGoodsService extends IService<OrderGoods> {
    void addNewGoods(OrderGoods orderGoods);
    List<OrderGoods> getOrderInfo(String orderId, OrderQuery orderQuery);
    void removeOrderGood(String id);
}
