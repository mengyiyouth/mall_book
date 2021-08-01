package com.mengyi.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.entity.BookClassify;
import com.mengyi.entity.excel.ClassifyData;
import com.mengyi.entity.classify.OneClassify;
import com.mengyi.entity.classify.TwoClassify;
import com.mengyi.listener.ClassifyExcelListener;
import com.mengyi.mapper.BookClassifyMapper;
import com.mengyi.service.BookClassifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 图书类别 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-23
 */
@Service
public class BookClassifyServiceImpl extends ServiceImpl<BookClassifyMapper, BookClassify> implements BookClassifyService {

    @Override
    public void saveClassify(MultipartFile file, BookClassifyService bookClassifyService) {
        try{
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, ClassifyData.class, new ClassifyExcelListener(bookClassifyService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<OneClassify> getAllOneTwoClassify() {
        //查询一级分类
        QueryWrapper<BookClassify> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id", "0");
        List<BookClassify> oneClassifyList = baseMapper.selectList(wrapperOne);

        //查询所有二级分类
        QueryWrapper<BookClassify> wrapperTwo = new QueryWrapper<>();
//        不等于0就是二级分类
        wrapperTwo.ne("parent_id", "0");
        List<BookClassify> twoClassifyList = baseMapper.selectList(wrapperTwo);

        //封装一级分类
        List<OneClassify> finalSubjectList = new ArrayList<>();
        for(int i = 0; i < oneClassifyList.size(); i++){
            //得到oneClassifyList每个bookClassify对象
            BookClassify bookClassify = oneClassifyList.get(i);
            OneClassify oneClassify = new OneClassify();

            BeanUtils.copyProperties(bookClassify, oneClassify);

            //封装二级分类
            List<TwoClassify> twoFinalClassifyList = new ArrayList<>();
            for(int j = 0; j < twoClassifyList.size(); j++){
                BookClassify twoBookClassify = twoClassifyList.get(j);
                //判断二级分类的parentid
                if(twoBookClassify.getParentId().equals(bookClassify.getId())){
                    TwoClassify twoClassify = new TwoClassify();
                    BeanUtils.copyProperties(twoBookClassify, twoClassify);
                    twoFinalClassifyList.add(twoClassify);
                }
            }
            oneClassify.setChildren(twoFinalClassifyList);

            //放入final集合
            finalSubjectList.add(oneClassify);
        }
        //封装二级分类
        return finalSubjectList;
    }
}
