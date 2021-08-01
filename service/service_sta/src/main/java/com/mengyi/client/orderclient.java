package com.mengyi.client;

import com.mengyi.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author mengyiyouth
 * @date 2021/5/12 16:08
 **/
@Component
@FeignClient(name = "service-order", fallback = orderclientimpl.class)
public interface orderclient {
    //统计某一天的销售量
    @GetMapping("/orderservice/order/countOrderDaily/{day}")
    public R countOrderDaily(@PathVariable("day") String day);
}
