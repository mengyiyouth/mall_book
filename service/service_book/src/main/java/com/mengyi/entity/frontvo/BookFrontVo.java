package com.mengyi.entity.frontvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/4/20 21:39
 **/
@Data
public class BookFrontVo {
    @ApiModelProperty(value = "书名")
    private String title;

    @ApiModelProperty(value = "一级类别id")
    private String classifyParentId;

    @ApiModelProperty(value = "二级类别id")
    private String classifyId;

    @ApiModelProperty(value = "销量排序")
    private String buyCountSort;

    @ApiModelProperty(value = "最新时间排序")
    private String gmtCreateSort;

    @ApiModelProperty(value = "价格排序")
    private String priceSort;
}
