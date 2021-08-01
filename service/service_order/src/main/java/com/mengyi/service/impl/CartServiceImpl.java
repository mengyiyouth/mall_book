package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.client.EduClient;
import com.mengyi.entity.Cart;
import com.mengyi.mapper.CartMapper;
import com.mengyi.ordervo.BookWebVoOrder;
import com.mengyi.ordervo.CourseWebVoOrder;
import com.mengyi.service.CartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-07
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements CartService {

    @Autowired
    EduClient eduClient;

    @Override
    public String addCart(String goodsId, String memberIdByJwtToken) {
        //远程调用获取商品信息
        BookWebVoOrder bookInfoOrder = eduClient.getBookInfoOrder(goodsId);
        //先在数据库中进行查找看这个商品是是否已经在当前用户的购物车中
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", memberIdByJwtToken);
        wrapper.eq("goods_id", goodsId);
        Cart queryCart = baseMapper.selectOne(wrapper);
        if(queryCart != null){
            //如果查询到不为空，那么直接将数量进行添加可以
            String tmp = queryCart.getCount();
            int t = Integer.valueOf(tmp);
            t++;
            queryCart.setCount(t + "");
            baseMapper.update(queryCart, wrapper);
            return queryCart.getId();
        }
        Cart cart = new Cart();
        //设置用户id
        cart.setUserId(memberIdByJwtToken);
        //设置商品id
        cart.setGoodsId(goodsId);
//        设置封面
        cart.setCover(bookInfoOrder.getCover());
        //设置价格
        cart.setPrice(bookInfoOrder.getPrice());
        //设置书名
        cart.setTitle(bookInfoOrder.getTitle());
        cart.setCount("1");
        baseMapper.insert(cart);
        return cart.getId();
    }

    @Override
    public List<Cart> getCart(String memberIdByJwtToken) {
        //先根据用户id获取到所有得CartInfo
        List<Cart> cartLists = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", memberIdByJwtToken);
        cartLists = baseMapper.selectList(wrapper);
        //远程调用获取课程信息
//        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);
        return cartLists;
    }


//得到购物车大小
    @Override
    public Integer getCartSize(String memberId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", memberId);
        return baseMapper.selectCount(wrapper);
    }


}
