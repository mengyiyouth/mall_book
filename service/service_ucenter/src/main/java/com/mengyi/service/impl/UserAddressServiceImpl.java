package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.entity.UserAddress;
import com.mengyi.mapper.UserAddressMapper;
import com.mengyi.service.UserAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-05-13
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Override
    public List<UserAddress> getAllAddress(String memberId) {
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", memberId);
        List<UserAddress> userAddresses = baseMapper.selectList(wrapper);
        return userAddresses;
    }
}
