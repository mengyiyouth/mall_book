package com.mengyi.confi;

import com.mengyi.R;
import com.mengyi.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mengyiyouth
 * @date 2021/1/26 10:57
 **/
@RestController
@RequestMapping("/malloss/fileoss")
//@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    @GetMapping("/caonima")
    public String a(){
        return "caonimabi";
    }
    //上传头像的方法
    @PostMapping
    public R uploadOssFile(MultipartFile file) {
//        返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
//        获取上传文件
        return R.ok().data("url", url);
    }
}
