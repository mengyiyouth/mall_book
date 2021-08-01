package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.JwtUtils;
import com.mengyi.MD5;
import com.mengyi.entity.UcenterMember;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.mapper.UcenterMemberMapper;
import com.mengyi.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengyi.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-26
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember ucenterMember) {
        //获取登录的手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();

        //手机号和密码非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001, "登录失败");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if(mobileMember == null){
            //没有数据
            throw new GuliException(20001, "手机号不存在");
        }
        //判断密码
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){
            throw new GuliException(20001, "密码错误");
        }
        //判断用户是否禁用
        if(mobileMember.getIsDisabled()){
            throw new GuliException(20001, "用户被禁用");
        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
//        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String password = registerVo.getPassword();

        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickname)){
            throw new GuliException(20001, "注册失败");
        }

        //判断验证码
//        String redisCode = redisTemplate.opsForValue().get(mobile).toString();
//        if(!code.equals(redisCode)){
//            throw new GuliException(20001, "验证码错误");
//        }
        //手机号不能重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0){
            throw new GuliException(20001, "注册失败");
        }
        //数据添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("http://www.mengyiyouth.cn:9000/picture/womanFree.jpg");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    @Override
    public Integer countRegisterDay(String day) {
//        return 0;
        return baseMapper.countRegisterDay(day);
    }
}
