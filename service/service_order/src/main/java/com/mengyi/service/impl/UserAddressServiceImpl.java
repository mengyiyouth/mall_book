package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.client.UcenterClient;
import com.mengyi.entity.TOrder;
import com.mengyi.entity.UserAddress;
import com.mengyi.mapper.UserAddressMapper;
import com.mengyi.ordervo.UcenterMemberOrder;
import com.mengyi.service.TOrderService;
import com.mengyi.service.UserAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-25
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
    @Autowired
    UcenterClient ucenterClient;
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    TOrderService orderService;

    @Override
    public void addAddress(UserAddress userAddress, String memberId) {
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);
        userAddress.setMemberId(memberId);
        userAddress.setMobile(userInfoOrder.getMobile());
        userAddress.setNickName(userInfoOrder.getNickname());
        userAddressService.save(userAddress);
    }

    @Override
    public List<UserAddress> getAllAddress(String memberId) {
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        List<UserAddress> userAddresses = baseMapper.selectList(wrapper);
        return userAddresses;
    }

    @Override
    public void updateOrderAddress(UserAddress userAddress, String orderId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        TOrder one = orderService.getOne(wrapper);
        one.setAddress(userAddress.getAddress());
        one.setPostCode(userAddress.getPostCode());
        one.setNickName(userAddress.getNickName());
        one.setMobile(userAddress.getMobile());
        orderService.updateById(one);

    }
}
