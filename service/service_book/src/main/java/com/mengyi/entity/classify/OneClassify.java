package com.mengyi.entity.classify;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengyiyouth
 * @date 2021/1/27 10:44
 **/
//一级分类
@Data
public class OneClassify {
    private String id;

    private String title;

    private List<TwoClassify> children = new ArrayList<>();
}
