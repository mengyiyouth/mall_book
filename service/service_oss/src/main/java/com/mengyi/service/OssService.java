package com.mengyi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author mengyiyouth
 * @date 2021/1/26 10:57
 **/

public interface OssService {

    String uploadFileAvatar(MultipartFile file);
}
