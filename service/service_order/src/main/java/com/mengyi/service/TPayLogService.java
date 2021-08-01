package com.mengyi.service;

import com.mengyi.entity.TOrder;
import com.mengyi.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-02-08
 */
public interface TPayLogService extends IService<TPayLog> {

    Map createNative(String orderNo);

    Map<String, String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String, String> map);

    TOrder toPay(String orderNo, String id);
}
