package com.mengyi.client;

import com.mengyi.UcenterMemberPay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author mengyiyouth
 * @date 2021/2/7 21:02
 **/
@Component
@FeignClient(name = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/eduucenter/member/getInfoUc/{id}")
    public UcenterMemberPay getUcenter(@PathVariable("id") String id);
}
