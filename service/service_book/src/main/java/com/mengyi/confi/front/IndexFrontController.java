package com.mengyi.confi.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengyi.R;
import com.mengyi.entity.MallBook;
import com.mengyi.service.MallBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mengyiyouth
 * @date 2021/3/4 10:17
 **/
@RestController
//@CrossOrigin
@RequestMapping("/bookservice/indexfront")
public class IndexFrontController {
    @Autowired
    MallBookService mallBookService;
    //查询前8热门图书
    //加入Redis缓存中
    @Cacheable(value="index", key="'hotBook'")
    @GetMapping("/index")
    public R index(){
        QueryWrapper<MallBook> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("buy_count");
        wrapper.last("limit 8");
        List<MallBook> bookList = mallBookService.list(wrapper);

        return R.ok().data("bookList", bookList);
    }
}
