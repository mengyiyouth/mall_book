package com.mengyi.mapper;

import com.mengyi.entity.MallBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengyi.entity.frontvo.BookWebVo;

/**
 * <p>
 * 图书 Mapper 接口
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-18
 */
public interface MallBookMapper extends BaseMapper<MallBook> {
    BookWebVo getBaseBookInfo(String bookId);

}
