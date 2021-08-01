package com.mengyi.confi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.entity.OrderGoods;
import com.mengyi.entity.TOrder;
import com.mengyi.service.OrderGoodsService;
import com.mengyi.service.TOrderService;
import com.mengyi.utils.OrderNoUtil;
import com.mengyi.vo.AllOrdersAndGoods;
import com.mengyi.vo.OrderGoodsQuery;
import com.mengyi.vo.OrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/orderservice/order")
//@CrossOrigin
public class TOrderController {
    @Autowired
    private TOrderService orderService;

    @Autowired
    private OrderGoodsService orderGoodsService;


    @GetMapping("/getOrderDetail/{id}")
    public R getOrder(@PathVariable String id){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", id);

        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("order", order);
    }

    //这里是购买一件商品生成订单的方法
    //生成订单的方法
    //根据图书id和用户id创建订单，返回订单id
    @PostMapping("/createOrder")
    public R saveOrder(@RequestParam(value = "ids[]") String[] ids, @RequestParam(value = "counts[]") String[] counts,
                       HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        String orderNo = orderService.createOrder(ids, counts, memberId);
        if(orderNo.equals("库存不足")){
            return R.error().code(29000).message("商品库存不足");
        }
        return R.ok().data("orderNo", orderNo);
    }

    //根据订单id查询该订单下的所有商品信息
    @GetMapping("/getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        List<OrderGoods> list = orderService.getOrders(orderId, null);
        return R.ok().data("list", list);
    }

    //根据课程Id和用户id查询订单表中订单状态
    @GetMapping("/isBuyCourse/{courseId}/{memberId}")
    public Boolean isBuyCourse(@PathVariable String courseId, @PathVariable String memberId) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);//1代表已经支付
        int count = orderService.count(wrapper);
        if(count > 0){
            //已经支付
            return true;
        }else{
            return false;
        }
    }

    //根据订单id查询所有的订单信息
    @PostMapping("/pageOrderCondition/{current}/{limit}")
    public R getAllOrders(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) OrderQuery orderQuery){
        //创建page对象
        Page<TOrder> pageOrder = new Page<>(current, limit);
        //构建条件
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();

        String status = orderQuery.getStatus();
        //收货人
        String nickName = orderQuery.getNickName();
        //订单
        String orderNo = orderQuery.getOrderId();

        //判断是否为空
        if(!StringUtils.isEmpty(orderNo)){
            wrapper.like("order_no", orderNo);
        }
        if(!StringUtils.isEmpty(nickName)){
            //构建条件
            wrapper.like("nickname", nickName);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        orderService.page(pageOrder, wrapper);
        //这里莫名其妙看不了总数
        long total = pageOrder.getTotal();
        List<TOrder> records = pageOrder.getRecords();
//        long total = records.size();
        return  R.ok().data("total", total).data("rows", records);
    }

    @PostMapping("/delivery")
    public R deliverOrder(@RequestBody TOrder order) {
        //进行发货,这里后续还需要修改
        int i = 0;
        String postId = OrderNoUtil.getOrderNo();
        order.setPostId(postId);
        order.setStatus(2);
        orderService.updateById(order);
        //对订单信息进行更新
        return R.ok();
    }

    @GetMapping("/getOrderByNo/{orderId}")
    public R getOrderByNo (@PathVariable String orderId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_no", orderId);
        TOrder order = orderService.getOne(wrapper);
        return R.ok().data("list", order);
    }

    //获取申请退货的订单信息
    @PostMapping("/pageReturnOrderCondition/{current}/{limit}")
    public R getReturnOrder(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) OrderGoodsQuery orderGoodsQuery){
        //创建page对象
        Page<OrderGoods> pageOrderGoods = new Page<>(current, limit);
        //构建条件
        QueryWrapper<OrderGoods> wrapper = new QueryWrapper<>();
//        状态
        String status = orderGoodsQuery.getStatus();
//        申请时间
        String begin = orderGoodsQuery.getBeginTime();
//        处理时间
        String solve = orderGoodsQuery.getSolveTime();

//        订单号
        String orderNo = orderGoodsQuery.getOrderNo();

        //判断是否为空
        if(!StringUtils.isEmpty(orderNo)){
            wrapper.like("order_no", orderNo);
        }
        if(!StringUtils.isEmpty(status)){
            //构建条件
            wrapper.eq("status", status);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.eq("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(solve)){
            wrapper.eq("gmt_modified", solve);
        }

        wrapper.ge("status", "4");
        //排序
        wrapper.orderByDesc("gmt_create");

        orderGoodsService.page(pageOrderGoods, wrapper);
        long total = pageOrderGoods.getTotal();
        List<OrderGoods> records = pageOrderGoods.getRecords();
        return  R.ok().data("total", total).data("rows", records);
    }

//    查询该用户的所有订单信息
    @PostMapping("/getAllOrders")
    public R getAllOrdersFront(HttpServletRequest request, @RequestBody OrderQuery orderQuery){
        //        获取该用户的所有订单和商品信息
        List<AllOrdersAndGoods> allOrders = orderService.getAllOrder(JwtUtils.getMemberIdByJwtToken(request), orderQuery);
        return R.ok().data("list", allOrders);
    }

    //取消订单，对order_goods表和order表进行更新
    @DeleteMapping("/cancleOrder/{id}")
    public R deleteGoodById(@PathVariable String id){
//        orderGoodsService.removeOrderGood(id);
        TOrder order = orderService.getById(id);
        orderService.rollBackBuyCountAndReverse(order);
        orderService.removeById(id);
//        orderGoodsService.removeById(id);
        return R.ok();
    }

    @DeleteMapping("/deleteOrder/{id}")
    public R deleteOrder(@PathVariable String id){
        orderService.removeById(id);
        return R.ok();
    }

    //确认收货，订单状态改到3
    @GetMapping("confirmReceive/{id}")
    public R receiveGoodById(@PathVariable String id){
//        orderGoodsService.removeOrderGood(id);
        TOrder order = orderService.getById(id);
        order.setStatus(3);
//        同时把每一个商品的状态都设置为3已收货状态
        List<OrderGoods> list = orderService.getOrders(order.getOrderNo(), null);
            //修改OrderGoods表中的状态
            for(OrderGoods orderGoods : list){
                orderGoods.setStatus(3);
                orderGoodsService.updateById(orderGoods);
            }
        orderService.updateById(order);
        return R.ok();
    }

    //统计每天的销售量
    @GetMapping("/countOrderDaily/{day}")
    public R countOrderDaily(@PathVariable("day") String day){
        //使用sql语句实现
        Integer count = orderService.countSaleDay(day);
        return R.ok().data("countSale", count);
    }

}

