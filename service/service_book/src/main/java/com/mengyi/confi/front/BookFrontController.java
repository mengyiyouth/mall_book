package com.mengyi.confi.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengyi.R;
import com.mengyi.entity.MallBook;
import com.mengyi.entity.frontvo.BookFrontVo;
import com.mengyi.entity.frontvo.BookWebVo;
import com.mengyi.ordervo.BookWebVoOrder;
import com.mengyi.service.MallBookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mengyiyouth
 * @date 2021/4/20 21:34
 **/
@RestController
@RequestMapping("/bookservice/bookfront")
public class BookFrontController {
    @Autowired
    MallBookService mallBookService;

    @Autowired
    RedisTemplate redisTemplate;

//    前台图书信息分页查询
    @PostMapping("/getFrontBookList/{page}/{limit}")
    public R getFrontBookList(@PathVariable long page, @PathVariable long limit, @RequestBody(required = false) BookFrontVo bookFrontVo){
        Page<MallBook> pageBook = new Page(page, limit);
        Map<String, Object> map = mallBookService.getBookFromList(pageBook, bookFrontVo);
        return  R.ok().data(map);
    }

    //图书详情的方法
    //将图书详情也放到Redis中进行缓存
//    @Cacheable(value="detail", key="'Book'")
    @GetMapping("/getFrontBookInfo/{bookId}")
    public R getFrontBookInfo(@PathVariable String bookId, HttpServletRequest request){
        BookWebVo bookWebVo = mallBookService.getBaseBookInfo(bookId);
        if(redisTemplate.opsForValue().get(bookId) != null){
            BookWebVo res = (BookWebVo)redisTemplate.opsForValue().get(bookId);
            return R.ok().data("bookWebVo", res);
        }
        redisTemplate.opsForValue().set(bookId, bookWebVo, 60, TimeUnit.SECONDS);
        return R.ok().data("bookWebVo", bookWebVo);
    }

    //根据图书id查询图书信息
    @PostMapping("/getBookInfoOrder/{id}")
    public BookWebVoOrder getBookInfoOrder(@PathVariable String id){
        MallBook mallBook = mallBookService.getById(id);
        BookWebVoOrder bookWebVoOrder = new BookWebVoOrder();
        BeanUtils.copyProperties(mallBook, bookWebVoOrder);
        return bookWebVoOrder;
    }

    @PostMapping("updateBookCountAndReverse/{id}/{buyCount}")
    public int updateBookCountAndReverse(@PathVariable String id, @PathVariable Long buyCount){
        MallBook mallBook = mallBookService.getById(id);
        Long reverse = mallBook.getReverse();
        Long count = mallBook.getBuyCount();
        if(buyCount > reverse){
            //库存量不足无法购买
            return -1;
        }
        mallBook.setBuyCount(count + buyCount);
        mallBook.setReverse(reverse - buyCount);
        mallBookService.updateById(mallBook);
        return 0;
    }

    @PostMapping("rollBackReverse/{id}/{buyCount}")
    public void rollBackReverse(@PathVariable String id, @PathVariable Long buyCount){
        MallBook mallBook = mallBookService.getById(id);
        Long reverse = mallBook.getReverse();
        Long count = mallBook.getBuyCount();
        mallBook.setBuyCount(count - buyCount);
        mallBook.setReverse(reverse + buyCount);
        mallBookService.updateById(mallBook);
    }


}
