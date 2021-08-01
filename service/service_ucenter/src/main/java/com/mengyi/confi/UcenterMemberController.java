package com.mengyi.confi;


import com.mengyi.JwtUtils;
import com.mengyi.MD5;
import com.mengyi.R;
import com.mengyi.UcenterMemberPay;
import com.mengyi.entity.UcenterMember;
import com.mengyi.ordervo.UcenterMemberOrder;
import com.mengyi.service.UcenterMemberService;
import com.mengyi.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-26
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduucenter/member")
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        String fuck = ucenterMemberService.login(ucenterMember);
        Map<String, Object> map = new HashMap<>();
        map.put("fuck", fuck);
        return R.ok().data(map);
    }

    @GetMapping("/denglu")
    public R dengLu(){
//        String token = ucenterMemberService.login(ucenterMember);
        Map<String, Object> map = new HashMap<>();
        map.put("token", "你麻痹");
        return R.ok().data(map);
    }

    //注册
    @PostMapping("/register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("/getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember member = ucenterMemberService.getById(memberId);
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", member);
        return R.ok().data("userInfo", member);
    }

    //根据用户id获取用户信息,评论
    @PostMapping("/getInfoUc/{id}")
    public UcenterMemberPay getInfo(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberPay member = new UcenterMemberPay();
        BeanUtils.copyProperties(ucenterMember,member);
        return member;
    }

//    更新用户信息
    @PostMapping("/updateMemberInfo")
    public R updateMember(@RequestBody UcenterMember ucenterMember){
        //这是数据苦衷加过密的密码
        String pass = ucenterMemberService.getById(ucenterMember.getId()).getPassword();
        //需要先判断密码是否更改
        if(!ucenterMember.getPassword().equals(pass)){
            //密码更改
            ucenterMember.setPassword(MD5.encrypt(ucenterMember.getPassword()));
        }
        ucenterMemberService.updateById(ucenterMember);
        return R.ok().message("更新成功");
    }

    //支付
    @PostMapping("/getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        //根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberOrder member = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,member);
        return member;
    }

    //查询某一天的注册人数
    @GetMapping("/countRegister/{day}")
    public R countRegister(@PathVariable String day){
        //使用sql语句实现
        Integer count = ucenterMemberService.countRegisterDay(day);
        return R.ok().data("countRegister", count);

    }

    //更新用户余额信息
    @PostMapping("/updateBalance/{id}/{balance}")
    public void updateBalance(@PathVariable String id,@PathVariable BigDecimal balance){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        BigDecimal tmp = ucenterMember.getBalance();
        tmp = tmp.subtract(balance);
        ucenterMember.setBalance(tmp);
        ucenterMemberService.updateById(ucenterMember);
    }

    @PostMapping("/increaseBalance/{id}/{balance}")
    public void increaseBalance(@PathVariable String id,@PathVariable BigDecimal balance){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        BigDecimal tmp = ucenterMember.getBalance();
        tmp = tmp.add(balance);
        ucenterMember.setBalance(tmp);
        ucenterMemberService.updateById(ucenterMember);
    }




}

