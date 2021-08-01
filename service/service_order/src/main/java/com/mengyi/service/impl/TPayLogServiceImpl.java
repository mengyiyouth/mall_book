package com.mengyi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.mengyi.client.UcenterClient;
import com.mengyi.entity.OrderGoods;
import com.mengyi.entity.TOrder;
import com.mengyi.entity.TPayLog;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.mapper.TPayLogMapper;
import com.mengyi.ordervo.UcenterMemberOrder;
import com.mengyi.service.OrderGoodsService;
import com.mengyi.service.TOrderService;
import com.mengyi.service.TPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengyi.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-02-08
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;

    @Autowired
    UcenterClient ucenterClient;

    @Autowired
    private OrderGoodsService orderGoodsService;

    @Override
    public Map createNative(String orderNo) {
        try{
            //根据订单号查询订单信息
//            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
//            wrapper.eq("order_no", orderNo);
//            TOrder order = orderService.getOne(wrapper);
            //得到该订单下所有的商品
            List<OrderGoods> list = orderService.getOrders(orderNo, null);
            String title = "";
            BigDecimal totalFee = new BigDecimal("0");
            for(OrderGoods t : list){
                title += t.getGoodTitle();
                totalFee = totalFee.add(t.getTotalFee());
            }
//            order.setTotalFee(totalFee);
            //使用Map设置生成二维码需要参数
            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
//            m.put("body", order.getCourseTitle());//课程标题
            m.put("body", "亚当书城订单");//课程标题
            m.put("out_trade_no", orderNo);//订单号
//            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            //金额
            m.put("total_fee", totalFee.multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");
            //发送httpclient请求，传递xml格式，微信支付提供固定的地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //得到发送请求返回结果

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
//            map.put("course_id", order.getCourseId());
//            map.put("total_fee", order.getTotalFee());
//            map.put("course_id", order.getCourseId());
            map.put("total_fee", totalFee);
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));
            return map;
        }catch(Exception e){
            throw new GuliException(20001, "生成二维码失败");
        }
    }

    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(wrapper);

//        BigDecimal totalFee = new BigDecimal("0");
        //查询到所有的订单，统一进行状态更改
//        List<TOrder> list = orderService.getOrders(orderNo);
//        for(TOrder order : list){
//            totalFee = totalFee.add(order.getTotalFee());
        if(order.getStatus().intValue() == 1)
            return;
        order.setStatus(1);
        orderService.updateById(order);
//        }
        //记录支付日志
        TPayLog payLog=new TPayLog();
//        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setOrderNo(orderNo);//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型,1是微信支付
//        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);//插入到支付日志表
    }

    @Override
    public TOrder toPay(String orderNo, String id) {
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        TOrder order = orderService.getOne(wrapper);
        UcenterMemberOrder memberOrder = ucenterClient.getUserInfoOrder(id);

        if(memberOrder.getBalance().compareTo(order.getTotalFee()) < 0){
            throw new GuliException(20001, "余额不足请充值");
        }else{
            //可以支付
            //修改用户的余额
            BigDecimal  balance = order.getTotalFee();
            ucenterClient.updateBalance(id, balance);
//            List<OrderGoods> list = orderService.getOrders(orderNo);
//            //修改OrderGoods表中的状态
//            for(OrderGoods orderGoods : list){
//                orderGoods.setStatus(1);
//                orderGoodsService.updateById(orderGoods);
//            }
            //修改订单的支付状态,已支付
            order.setStatus(1);
            orderService.updateById(order);
            //payLog表进行更新
//            TPayLog payLog=new TPayLog();
////        payLog.setOrderNo(order.getOrderNo());//支付订单号
//            payLog.setOrderNo(orderNo);//支付订单号
//            payLog.setPayTime(new Date());
//            payLog.setPayType(1);//支付类型,1是微信支付
////        payLog.setTotalFee(order.getTotalFee());//总金额(分)
//            payLog.setTotalFee(order.getTotalFee());//总金额(分)
//            payLog.setTradeState("SUCCESS");//支付状态
            return order;
//            payLog.setTransactionId(map.get("transaction_id"));
//            payLog.setAttr(JSONObject.toJSONString(map));
        }
    }
}
