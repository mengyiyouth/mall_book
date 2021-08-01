package com.mengyi.client;

import com.mengyi.ordervo.BookWebVoOrder;
import com.mengyi.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author mengyiyouth
 * @date 2021/2/8 8:43
 **/
@Component
@FeignClient("service-book")
public interface EduClient {
    @PostMapping("/bookservice/bookfront/getBookInfoOrder/{id}")
    public BookWebVoOrder getBookInfoOrder(@PathVariable("id") String id);


    @PostMapping("/bookservice/bookfront/updateBookCountAndReverse/{id}/{buyCount}")
    int updateBookCountAndReverse(@PathVariable("id") String id, @PathVariable("buyCount") Long buyCount);

    @PostMapping("/bookservice/bookfront/rollBackReverse/{id}/{buyCount}")
    void rollBackReverse(@PathVariable("id") String id, @PathVariable("buyCount") Long buyCount);
}
