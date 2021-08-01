package com.mengyi.confi.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.JwtUtils;
import com.mengyi.R;
import com.mengyi.UcenterMemberPay;
import com.mengyi.client.UcenterClient;
import com.mengyi.entity.MallComment;
import com.mengyi.service.MallCommentService;
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
 * @since 2021-04-24
 */
@RestController
@RequestMapping("/bookservice/comment")
public class MallCommentController {
    @Autowired
    private MallCommentService mallCommentService;

    @Autowired
    private UcenterClient ucenterClient;

    //根据课程id查询评论列表
    @ApiOperation(value = "评论分页列表")
    @GetMapping("/{page}/{limit}/{bookId}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            @PathVariable String bookId) {
        Page<MallComment> pageParam = new Page<>(page, limit);

        QueryWrapper<MallComment> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id",bookId);

        mallCommentService.page(pageParam,wrapper);

        List<MallComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }




    @ApiOperation(value = "添加评论")
    @PostMapping("/auth/save")
    public R save(@RequestBody MallComment mallComment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        mallComment.setMemberId(memberId);

        UcenterMemberPay ucenterInfo = ucenterClient.getUcenter(memberId);

        mallComment.setNickname(ucenterInfo.getNickname());
        mallComment.setAvatar(ucenterInfo.getAvatar());

        mallCommentService.save(mallComment);
        return R.ok();
    }

}

