package com.mengyi.confi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.entity.Cart;
import com.mengyi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-07
 */
@RestController
@RequestMapping("/orderservice/cart")
//@CrossOrigin
public class CartController {
    @Autowired
    CartService cartService;

    //    商品添加到购物车
    @PostMapping("/saveCart/{goodsId}")
    public R saveCart(@PathVariable String goodsId, HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }

        Integer countCart = cartService.getCartSize(memberId);
        if(countCart != null && countCart >= 99){
            return R.error().code(28004).message("购物车已满");
        }
        //先判断购物车中是否已经有了这个商品
        QueryWrapper<Cart> wrapper = new QueryWrapper<>();

        wrapper.eq("goods_id", goodsId);
        wrapper.eq("user_id", memberId);
        Cart one = cartService.getOne(wrapper);
        if(one != null){
            //有这个商品，则数量++
            String count = one.getCount();
            int tmp = Integer.valueOf(count);
            tmp++;
            one.setCount(tmp + "");
            cartService.updateById(one);
            return R.ok().data("cartId", one.getId());
        }

        String cartId = cartService.addCart(goodsId, memberId);

        return R.ok().data("cartId", cartId);
    }

//    查询购物车信息
    @GetMapping("/getCart")
    public R getCartInfo(HttpServletRequest request){
        List<Cart> cartInfo = cartService.getCart(JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("cartInfo", cartInfo);
    }

    @PostMapping("/deleteByGoodsId")
    public R deleteCartInfo(@RequestParam(value = "ids[]") String[] ids){
        //这里传递过来的是cartId数组
        for(String id : ids){
            cartService.removeById(id);
        }
        return R.ok();
    }

    //更新购物车商品的数量信息
    @PostMapping("/updateCount")
    public R updateGoodsCount(@RequestBody Cart newCart){
        boolean flag = cartService.updateById(newCart);
        if(flag){
            return R.ok();
        }else{
            return R.error();
        }
    }



}

