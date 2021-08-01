package com.mengyi.confi;


import com.mengyi.R;

import com.mengyi.entity.BookClassify;
import com.mengyi.entity.classify.OneClassify;
import com.mengyi.service.BookClassifyService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图书类别 前端控制器
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-23
 */
@RestController
@RequestMapping("/bookservice/classify")
//@CrossOrigin
public class BookClassifyController{

    @Autowired
    private BookClassifyService bookClassifyService;

    //添加图书分类
    @PostMapping("/addClassify")
    public R addSubject(MultipartFile file){
        bookClassifyService.saveClassify(file, bookClassifyService);
        return R.ok();
    }

    //图书分类列表(树形)
    @GetMapping("/getAllClassify")
    public R getAllClassify() {
        List<OneClassify> list = bookClassifyService.getAllOneTwoClassify();
        return R.ok().data("list", list);
    }

//    更新分类名称
    @PostMapping("/updateClassifyTitle")
    public R updateClassify(@RequestBody BookClassify bookClassify){
        bookClassifyService.updateById(bookClassify);
        return R.ok();
    }

}


