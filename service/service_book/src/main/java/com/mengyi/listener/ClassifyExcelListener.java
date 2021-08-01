package com.mengyi.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.entity.BookClassify;
import com.mengyi.entity.excel.ClassifyData;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.service.BookClassifyService;

/**
 * @author mengyiyouth
 * @date 2021/4/23 15:18
 **/
public class ClassifyExcelListener extends AnalysisEventListener<ClassifyData> {

    public BookClassifyService bookClassifyService;

    public ClassifyExcelListener() {
    }

    public ClassifyExcelListener(BookClassifyService classifyService) {
        this.bookClassifyService = classifyService;
    }

    @Override
    public void invoke(ClassifyData classifyData, AnalysisContext analysisContext) {

        if(classifyData == null) {
            throw new GuliException(20001, "文件数据为空");
        }

        //一行一行读取，每次读取有两个值，一级分类和二级分类
        BookClassify existOneClassify = this.existOneClassify(bookClassifyService, classifyData.getOneClassifyName());
        if(existOneClassify == null){
            //没有相同的一级分类，可以进行添加
            existOneClassify = new BookClassify();
            existOneClassify.setParentId("0");
            existOneClassify.setTitle(classifyData.getOneClassifyName());//设置一级分类名称
            //将一级分类添加到数据库
            bookClassifyService.save(existOneClassify);
        }

        //添加二级分类
        //获取一级分类的id值
        String pid = existOneClassify.getId();
        //判断是否有二级分类
        BookClassify existTwoClassify = this.existTwoClassify(bookClassifyService, classifyData.getTwoClassifyName(), pid);
        if(existTwoClassify == null){
            existTwoClassify = new BookClassify();
            existTwoClassify.setParentId(pid);
            existTwoClassify.setTitle(classifyData.getTwoClassifyName());//设置二级分类名称
            bookClassifyService.save(existTwoClassify);
        }
    }
    //判断一级分类不能重复添加
    private BookClassify existOneClassify(BookClassifyService bookClassifyService, String name){
        QueryWrapper<BookClassify> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", "0");
        BookClassify oneClassify = bookClassifyService.getOne(wrapper);
        return oneClassify;
    }
    //二级分类
    //相同的二级分类也只能有一个
    private BookClassify existTwoClassify(BookClassifyService bookClassifyService, String name, String pid){
        QueryWrapper<BookClassify> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name);
        wrapper.eq("parent_id", pid);
        BookClassify twoClassify = bookClassifyService.getOne(wrapper);
        return twoClassify;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    //读取excel中内容，一行一行进行读取
}
