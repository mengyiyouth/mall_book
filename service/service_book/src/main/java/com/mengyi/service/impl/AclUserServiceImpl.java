package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.JwtUtils;
import com.mengyi.MD5;
import com.mengyi.entity.AclUser;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.mapper.AclUserMapper;
import com.mengyi.service.AclUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-03-22
 */
@Service
public class AclUserServiceImpl extends ServiceImpl<AclUserMapper, AclUser> implements AclUserService {

    @Override
    public String login(AclUser aclUser) {
        //获取登录的用户名和密码
        String username = aclUser.getUsername();
        String password = aclUser.getPassword();


        QueryWrapper<AclUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        //从数据库中查询出用户名和密码
        AclUser findUser = baseMapper.selectOne(wrapper);
        if(findUser == null){
            //没有数据
            throw new GuliException(20001, "用户名不存在");
        }
        //判断密码
        if(!MD5.encrypt(password).equals(findUser.getPassword())){
            throw new GuliException(20001, "密码错误");
        }
        //判断用户是否禁用
//        if(findUser.getIsDisabled()){
//            throw new GuliException(20001, "登录失败");
//        }
        //登录成功
        //生成token字符串，使用jwt工具类
        String jwtToken = JwtUtils.getJwtToken(findUser.getId(), findUser.getNickName());
        return jwtToken;
    }
}
