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
 * @since 2021-05-13
 */
public interface UserAddressService extends IService<UserAddress> {

    List<UserAddress> getAllAddress(String memberId);
}
