package com.mengyi.client;

import com.mengyi.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

/**
 * @author mengyiyouth
 * @date 2021/2/8 8:43
 **/
@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //支付
    @PostMapping("/eduucenter/member/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable("id") String id);

    //更新余额信息
    @PostMapping("/eduucenter/member/updateBalance/{id}/{balance}")
    void updateBalance(@PathVariable String id,@PathVariable BigDecimal balance);

//    退款
    @PostMapping("/eduucenter/member/increaseBalance/{id}/{balance}")
    void increaseBalance(@PathVariable String id,@PathVariable BigDecimal balance);
}
