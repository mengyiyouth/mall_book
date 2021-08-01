package com.mengyi.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/4/18 19:37
 **/
@Data
public class BookQuery {
    @ApiModelProperty(value = "书名")
    private String title;

    @ApiModelProperty(value = "图书状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "销量升序和降序排序 1 正序 -1 降序")
    private Integer seq;

    @ApiModelProperty(value = "图书一级分类ID")
    private String classifyId;

    @ApiModelProperty(value = "图书专业父级ID")
    private String classifyParentId;
}
