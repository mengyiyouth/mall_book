package com.mengyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.entity.MallBook;
import com.mengyi.entity.MallBookDescription;
import com.mengyi.entity.frontvo.BookFrontVo;
import com.mengyi.entity.frontvo.BookWebVo;
import com.mengyi.entity.vo.MallBookInfoVo;
import com.mengyi.exceptionhandler.GuliException;
import com.mengyi.mapper.MallBookMapper;
import com.mengyi.service.MallBookDescriptionService;
import com.mengyi.service.MallBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图书 服务实现类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
@Service
public class MallBookServiceImpl extends ServiceImpl<MallBookMapper, MallBook> implements MallBookService {
    @Autowired
    MallBookDescriptionService mallBookDescriptionService;


    @Override
    public String saveBookInfo(MallBookInfoVo mallBookInfoVo) {

        MallBook mallBook = new MallBook();
        BeanUtils.copyProperties(mallBookInfoVo, mallBook);
        int insert = baseMapper.insert(mallBook);
        if(insert <= 0){
            throw new GuliException(20001, "添加图书信息失败");
        }

        //获取添加之后课程id
        String bid = mallBook.getId();
        //向课程简介表添加课程简介
        MallBookDescription mallBookDescription = new MallBookDescription();
        mallBookDescription.setDescription(mallBookInfoVo.getDescription());
        mallBookDescription.setId(bid);
        mallBookDescriptionService.save(mallBookDescription);

        return bid;
    }

    @Override
    public MallBookInfoVo getBookInfo(String id) {
        //查询图书
        MallBook mallBook = baseMapper.selectById(id);

        MallBookInfoVo mallBookInfoVo = new MallBookInfoVo();

        BeanUtils.copyProperties(mallBook, mallBookInfoVo);
        //查询描述表
        MallBookDescription mallBookDescription = mallBookDescriptionService.getById(id);

        mallBookInfoVo.setDescription(mallBookDescription.getDescription());

        return mallBookInfoVo;
    }

    @Override
    public void updateBookInfo(MallBookInfoVo mallBookInfoVo) {
        //修改book表
        MallBook mallBook = new MallBook();

        BeanUtils.copyProperties(mallBookInfoVo, mallBook);
        int update = baseMapper.updateById(mallBook);
        if(update == 0){
            //修改失败
            throw new GuliException(20001, "修改图书信息失败");
        }
        //修改描述表
        MallBookDescription mallBookDescription = new MallBookDescription();
        mallBookDescription.setId(mallBookInfoVo.getId());
        mallBookDescription.setDescription(mallBookInfoVo.getDescription());
        mallBookDescriptionService.updateById(mallBookDescription);
    }

    @Override
    public Map<String, Object> getBookFromList(Page<MallBook> pageBook, BookFrontVo bookFrontVo) {
        QueryWrapper<MallBook> wrapper = new QueryWrapper<>();
        //判断条件值是否为空
        if (!StringUtils.isEmpty(bookFrontVo.getClassifyParentId())) {
            wrapper.eq("classify_parent_id", bookFrontVo.getClassifyParentId());
        }
        if (!StringUtils.isEmpty(bookFrontVo.getClassifyId())) {
            wrapper.eq("classify_id", bookFrontVo.getClassifyId());
        }

        if (!StringUtils.isEmpty(bookFrontVo.getBuyCountSort())) {
            wrapper.orderByDesc("buy_count");
        }

        if (!StringUtils.isEmpty(bookFrontVo.getGmtCreateSort())) {
            wrapper.orderByDesc("gmt_create");
        }

        if (!StringUtils.isEmpty(bookFrontVo.getPriceSort())) {
            wrapper.orderByDesc("price");
        }

        if(!StringUtils.isEmpty(bookFrontVo.getTitle())){
            wrapper.like("title", bookFrontVo.getTitle());
        }
        wrapper.eq("status", "Normal");
        baseMapper.selectPage(pageBook, wrapper);

        List<MallBook> records = pageBook.getRecords();
        long current = pageBook.getCurrent();
        long pages = pageBook.getPages();
        long size = pageBook.getSize();
        long total = pageBook.getTotal();
        boolean hasNext = pageBook.hasNext();
        boolean hasPrevious = pageBook.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public BookWebVo getBaseBookInfo(String bookId) {
        return baseMapper.getBaseBookInfo(bookId);
    }
}
