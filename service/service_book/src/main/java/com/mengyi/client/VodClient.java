package com.mengyi.client;

import com.mengyi.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author mengyiyouth
 * @date 2021/2/2 12:56
 **/
@Component
@FeignClient(name = "server-vod", fallback = VodFileDegradeFeignClient.class)
public interface VodClient {
    //定义调用的方法路径
    //删除视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
