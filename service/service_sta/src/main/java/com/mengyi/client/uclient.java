package com.mengyi.client;

import com.mengyi.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author mengyiyouth
 * @date 2021/2/8 21:32
 **/
@Component
@FeignClient(name = "service-ucenter", fallback = uclientImpl.class)
public interface uclient {
    //统计某一天的注册人数
    @GetMapping("/eduucenter/member/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day);
}
