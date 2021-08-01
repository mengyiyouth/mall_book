package com.mengyi.service;

import com.mengyi.entity.BookClassify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mengyi.entity.classify.OneClassify;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 图书类别 服务类
 * </p>
 *
 * @author mengyiyouth
 * @since 2021-04-23
 */
public interface BookClassifyService extends IService<BookClassify> {

    void saveClassify(MultipartFile file, BookClassifyService bookClassifyService);

    List<OneClassify> getAllOneTwoClassify();
}
