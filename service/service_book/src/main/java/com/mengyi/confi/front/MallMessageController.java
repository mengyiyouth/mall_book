package com.mengyi.confi.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.UcenterMemberPay;
import com.mengyi.client.UcenterClient;
import com.mengyi.entity.MallComment;
import com.mengyi.entity.MallMessage;
import com.mengyi.service.MallMessageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-28
 */
@RestController
@RequestMapping("/bookservice/message")
public class MallMessageController {
    @Autowired
    private MallMessageService mallMessageService;

    @Autowired
    private UcenterClient ucenterClient;

    //查询留言列表
    @ApiOperation(value = "留言分页列表")
    @GetMapping("/{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<MallMessage> pageParam = new Page<>(page, limit);

        QueryWrapper<MallMessage> wrapper = new QueryWrapper<>();

        mallMessageService.page(pageParam,wrapper);

        List<MallMessage> messageList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", messageList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }




    @ApiOperation(value = "添加留言")
    @PostMapping("/auth/save")
    public R save(@RequestBody MallMessage mallMessage, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        mallMessage.setMemberId(memberId);

        UcenterMemberPay ucenterInfo = ucenterClient.getUcenter(memberId);

        mallMessage.setNickname(ucenterInfo.getNickname());
        mallMessage.setAvatar(ucenterInfo.getAvatar());

        mallMessageService.save(mallMessage);
        return R.ok();
    }

}

