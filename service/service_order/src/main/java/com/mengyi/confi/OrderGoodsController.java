package com.mengyi.confi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.R;
import com.mengyi.entity.OrderGoods;
import com.mengyi.entity.TOrder;
import com.mengyi.service.OrderGoodsService;
import com.mengyi.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-12
 */
@RestController
//@CrossOrigin
@RequestMapping("/orderservice/ordergoods")
public class OrderGoodsController {
    @Autowired
    private OrderGoodsService orderGoodsService;

    @Autowired
    private TOrderService orderService;

    //得到退货的商品的信息
    @GetMapping("/getReturnOrderInfo/{id}")
    public R getOrderInfo(@PathVariable String id){
        OrderGoods orderGoods = orderGoodsService.getById(id);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderGoods.getOrderNo());
        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("list", orderGoods).data("order", order);
    }

    //确认退货，对order_goods表和order表进行更新
    @PostMapping("/returnGood/{address}")
    public R confirmReturn(@RequestBody OrderGoods orderGoods){
        //主要进行两个状态的更改
        //order_goods中status从3，已收货到4.退货中
        orderGoods.setStatus(4);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderGoods.getOrderNo());
        TOrder order = orderService.getOne(wrapper);
//        状态设置为4，原始订单也进入退货状态
        order.setStatus(4);
//        order.setAddress(address);
//        在数据库中进行更新
        orderGoodsService.updateById(orderGoods);
        orderService.updateById(order);
        return R.ok();
    }

    //确认退货，对order_goods表和order表进行更新
    @DeleteMapping("/cancleOrder/{id}")
    public R deleteGoodById(@PathVariable String id){
//        orderGoodsService.removeOrderGood(id);
        orderGoodsService.removeById(id);
        return R.ok();
    }

//    同意退货处理
    @PostMapping("/agreeSolve")
    public R agreeSolve(@RequestBody OrderGoods orderGoods){
        orderGoods.setStatus(5);
        orderGoodsService.updateById(orderGoods);
        return R.ok();
    }

    @PostMapping("/refuseSolve")
    public R refuseSolve(@RequestBody OrderGoods orderGoods){
        orderGoods.setStatus(6);
        orderGoodsService.updateById(orderGoods);
        return R.ok();
    }



}

