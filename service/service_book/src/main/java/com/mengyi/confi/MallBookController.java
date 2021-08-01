package com.mengyi.confi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.R;
import com.mengyi.entity.MallBook;
import com.mengyi.entity.vo.*;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.service.MallBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 图书 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
@RestController
//@CrossOrigin
@RequestMapping("/bookservice/book")
public class MallBookController {

    @Autowired
    MallBookService mallBookService;


    //添加图书基本信息
    @PostMapping("/addBookInfo")
    public R addBookInfo(@RequestBody MallBookInfoVo mallBookInfoVo) {
        String id = mallBookService.saveBookInfo(mallBookInfoVo);
        return R.ok().data("bookId", id);
    }

    //条件查询+分页查询所有图书信息
    @PostMapping("/pageBookCondition/{current}/{limit}")
    public R pageBookCondition(@PathVariable long current, @PathVariable long limit, @RequestBody(required = false) BookQuery bookQuery){
        //创建page对象
        Page<MallBook> pageBook = new Page<>(current, limit);
        //构建条件
        QueryWrapper<MallBook> wrapper = new QueryWrapper<>();

        String title = bookQuery.getTitle();
        String status = bookQuery.getStatus();
        Integer seq = bookQuery.getSeq();
        String classifyParentId = bookQuery.getClassifyParentId();
        String classifyId = bookQuery.getClassifyId();
        //判断是否为空
        if(!StringUtils.isEmpty(title)){
            //构建条件
            wrapper.like("title", title);
        }
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status", status);
        }
        if(seq != null){
            if(seq > 0)
                wrapper.orderByAsc("buy_count");
            else
                wrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(classifyParentId)){
            wrapper.eq("classify_parent_id", classifyParentId);
        }
        if(!StringUtils.isEmpty(classifyId)){
            wrapper.eq("classify_id", classifyId);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        mallBookService.page(pageBook, wrapper);
        long total = pageBook.getTotal();
        List<MallBook> records = pageBook.getRecords();
        return  R.ok().data("total", total).data("rows", records);
    }

    //图书的发布与下架
    @GetMapping("/updateBookStatus/{id}")
    public R changeBookStatus(@PathVariable String id){
        MallBook mallBook = mallBookService.getById(id);
        if(mallBook.getStatus().equals(("Normal"))){
            mallBook.setStatus("Draft");
        }else{
            mallBook.setStatus("Normal");
        }
        mallBookService.updateById(mallBook);
        return R.ok();
    }

    //查询图书基本信息
    @GetMapping("/getBookInfo/{id}")
    public R getBookInfo(@PathVariable String id){
        MallBookInfoVo bookInfoVo = mallBookService.getBookInfo(id);
        return R.ok().data("bookInfoVo", bookInfoVo);
    }

    //修改图书基本信息
    @PostMapping("/updateBookInfo")
    public R updateBookInfo(@RequestBody MallBookInfoVo mallBookInfoVo){
        mallBookService.updateBookInfo(mallBookInfoVo);
        return R.ok();
    }

    @DeleteMapping("/deleteBookById/{id}")
    public R deleteBookInfo(@PathVariable String id){
        boolean flag = mallBookService.removeById(id);
        if(!flag)
            throw new GuliException(20001, "删除图书信息失败");
        return R.ok();
    }

}

