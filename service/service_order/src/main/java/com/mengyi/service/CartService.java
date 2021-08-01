package com.mengyi.service;

import com.mengyi.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-07
 */
public interface CartService extends IService<Cart> {

    String addCart(String goodsId, String memberIdByJwtToken);

    List<Cart> getCart(String memberIdByJwtToken);

    Integer getCartSize(String memberId);
}
