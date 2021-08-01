package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.client.EduClient;
import com.mengyi.client.UcenterClient;
import com.mengyi.entity.OrderGoods;
import com.mengyi.entity.TOrder;
import com.mengyi.mapper.TOrderMapper;
import com.mengyi.ordervo.BookWebVoOrder;
import com.mengyi.ordervo.CourseWebVoOrder;
import com.mengyi.ordervo.UcenterMemberOrder;
import com.mengyi.service.OrderGoodsService;
import com.mengyi.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengyi.utils.OrderNoUtil;
import com.mengyi.vo.AllOrdersAndGoods;
import com.mengyi.vo.OrderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-14
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    EduClient eduClient;

    @Autowired
    UcenterClient ucenterClient;

    @Autowired
    OrderGoodsService orderGoodsService;

    @Autowired
    TOrderService tOrderService;

    //生成订单的方法
    @Override
    public String createOrder(String[] ids, String[] counts, String memberId) {
        //远程调用获取用户信息，
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberId);

        //订单号只获取一次
        String returnOrderNo = OrderNoUtil.getOrderNo();
        BigDecimal totalFee = new BigDecimal("0");
        for(int j = 0; j < ids.length; j++){
            //将订单中的信息都加入到OrderGoods表中
            //查询到图书信息
            BookWebVoOrder bookInfoOrder = eduClient.getBookInfoOrder(ids[j]);
            //同时将图书的购买量+1和库存量-1
            //远程调用
            int res = eduClient.updateBookCountAndReverse(ids[j], Long.valueOf(counts[j]));
            if(res < 0){
                //库存不足
                return "库存不足";
            }

            //创建OrderGoods对象，向order中设置值
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setOrderNo(returnOrderNo);
            orderGoods.setGoodId(ids[j]);
            orderGoods.setCount(counts[j]);
            orderGoods.setGoodCover(bookInfoOrder.getCover());
            orderGoods.setGoodTitle(bookInfoOrder.getTitle());
            orderGoods.setSinglePrice(bookInfoOrder.getPrice());
            orderGoods.setNickName(userInfoOrder.getNickname());

            totalFee = totalFee.add(bookInfoOrder.getPrice().multiply(BigDecimal.valueOf(Integer.valueOf(counts[j]))));
            orderGoods.setTotalFee(bookInfoOrder.getPrice().multiply(BigDecimal.valueOf(Integer.valueOf(counts[j]))));
            orderGoodsService.addNewGoods(orderGoods);

        }
        TOrder order = new TOrder();
        order.setOrderNo(returnOrderNo);//订单号
//        order.setCourseId(ids[j]);//课程id
//        order.setCourseTitle(courseInfoOrder.getTitle());
//        order.setCourseCover(courseInfoOrder.getCover());
//        order.setTeacherName(courseInfoOrder.getTeacherName());
        //设置单价
//        order.setSinglePrice(courseInfoOrder.getPrice());
        //设置数量
//        order.setCount(counts[j]);
        //单价x数量
        order.setTotalFee(totalFee);
        order.setMemberId(memberId);
//        order.setMobile(userInfoOrder.getMobile());
//        order.setNickName(userInfoOrder.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);
        return returnOrderNo;//返回订单号
    }

    @Override
    public List<OrderGoods> getOrders(String orderId, OrderQuery orderQuery) {
        //从order_goods表中查询
        List<OrderGoods> list = orderGoodsService.getOrderInfo(orderId, orderQuery);
//        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("order_no", orderId);
//        List<TOrder> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public List<AllOrdersAndGoods> getAllOrder(String memberId, OrderQuery orderQuery){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("member_id", memberId);
        String status = orderQuery.getStatus();
        //这里有一个状态查询
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }
        List<TOrder> list = baseMapper.selectList(wrapper);
        List<AllOrdersAndGoods> res = new ArrayList<>();
        for(TOrder order : list){
            AllOrdersAndGoods allOrdersAndGoods = new AllOrdersAndGoods();
//            设置订单编号和订单时间
            List<OrderGoods> tmp = tOrderService.getOrders(order.getOrderNo(), orderQuery);
            if(tmp.size() != 0){
                allOrdersAndGoods.setId(order.getId());
                allOrdersAndGoods.setOrderNo(order.getOrderNo());
                allOrdersAndGoods.setGmtCreate(order.getGmtCreate());
                allOrdersAndGoods.setTotalFee(order.getTotalFee());
                allOrdersAndGoods.setStatus(order.getStatus());
                allOrdersAndGoods.setAllGoodsDetail(tmp);
                res.add(allOrdersAndGoods);
            }

        }
        return res;
    }

//    取消订单之后不仅要更新图书的购买量和库存
//    同时还要更新用户余额
    @Override
    public void rollBackBuyCountAndReverse(TOrder order) {
        List<OrderGoods> list = getOrders(order.getOrderNo(), null);
        BigDecimal total = new BigDecimal("0");
        for(OrderGoods orderGoods : list){
            eduClient.rollBackReverse(orderGoods.getGoodId(), Long.valueOf(orderGoods.getCount()));
            total = total.add(orderGoods.getTotalFee());
        }
        //退款
        //如果是还没有付款的时候取消那么不用退款
        if(order.getStatus() != 0)
            ucenterClient.increaseBalance(order.getMemberId(), total);
    }

    @Override
    public Integer countSaleDay(String day) {
        int i = 0;
        return baseMapper.getSaleSta(day);
    }
}

