package com.mengyi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.entity.MallBook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengyi.entity.frontvo.BookFrontVo;
import com.mengyi.entity.frontvo.BookWebVo;
import com.mengyi.entity.vo.MallBookInfoVo;

import java.util.Map;

/**
 * <p>
 * 图书 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
public interface MallBookService extends IService<MallBook> {


    String saveBookInfo(MallBookInfoVo mallBookInfoVo);

    MallBookInfoVo getBookInfo(String id);

    void updateBookInfo(MallBookInfoVo mallBookInfoVo);

    Map<String, Object> getBookFromList(Page<MallBook> pageBook, BookFrontVo bookFrontVo);

    BookWebVo getBaseBookInfo(String bookId);
}
