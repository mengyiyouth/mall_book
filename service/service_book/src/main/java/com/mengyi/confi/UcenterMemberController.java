package com.mengyi.confi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.R;
import com.mengyi.entity.AclUser;
import com.mengyi.entity.UcenterMember;
import com.mengyi.entity.vo.MemberQuery;
import com.mengyi.entity.vo.TeacherQuery;
import com.mengyi.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-20
 */
@RestController
@RequestMapping("/bookservice/member")
public class UcenterMemberController {


    @Autowired
    UcenterMemberService ucenterMemberService;

    //条件查询分页
    @PostMapping("/pageMemberCondition/{current}/{limit}")
    public R pageMemberCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false)MemberQuery memberQuery){
        //创建page对象
        Page<UcenterMember> pageMember = new Page<>(current, limit);
        //构建条件
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();

        String nickname = memberQuery.getNickname();
        String begin = memberQuery.getBegin();
        String end = memberQuery.getEnd();
        //判断是否为空
        if(!StringUtils.isEmpty(nickname)){
            //构建条件
            wrapper.like("nickname", nickname);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create", begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_modified", end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        ucenterMemberService.page(pageMember, wrapper);
        long total = pageMember.getTotal();
        List<UcenterMember> records = pageMember.getRecords();
        return  R.ok().data("total", total).data("rows", records);
    }

    @GetMapping("updateStatusById/{id}")
    public R updateStatusById(@PathVariable String id){
        UcenterMember byId = ucenterMemberService.getById(id);
//        状态反转
        boolean status = byId.getIsDisabled();
        byId.setIsDisabled(!status);
        ucenterMemberService.updateById(byId);
        return R.ok();
    }
}

