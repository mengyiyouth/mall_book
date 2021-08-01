package com.mengyi.service;

import com.mengyi.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-25
 */
public interface UserAddressService extends IService<UserAddress> {

    void addAddress(UserAddress userAddress, String memberId);

    List<UserAddress> getAllAddress(String memberId);

    void updateOrderAddress(UserAddress userAddress, String orderId);
}
