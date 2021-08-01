package com.mengyi.confi;


import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.entity.TOrder;
import com.mengyi.service.TPayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-02-08
 */
@RestController
//@CrossOrigin
@RequestMapping("/orderservice/paylog")
public class TPayLogController {

    @Autowired
    private TPayLogService payLogService;

    @GetMapping("/createPay/{orderNo}")
    public R createPay(@PathVariable String orderNo, HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        TOrder order = payLogService.toPay(orderNo, memberId);
        return R.ok().data("order", order);
    }

    //生成微信支付二维码接口
    //参数是订单号
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {
        //返回信息，包含二维码地址，还有其他相关信息

        Map map = payLogService.createNative(orderNo);
        System.out.println("----返回二维码map集合:" + map);
        return R.ok().data(map);
    }

    @GetMapping("/queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo) {
        //调用查询接口
        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        System.out.println("----返回查询订单状态集合:" + map);

        if (map == null) {//出错
            return R.error().message("支付出错");
        }
        if (map.get("trade_state").equals("SUCCESS")) {//如果成功
            //更改订单状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }

}

