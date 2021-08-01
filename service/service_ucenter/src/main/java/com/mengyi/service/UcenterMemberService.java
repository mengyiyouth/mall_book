package com.mengyi.service;

import com.mengyi.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengyi.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getOpenIdMember(String openid);

    Integer countRegisterDay(String day);
}
