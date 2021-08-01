package com.mengyi.entity.frontvo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mengyiyouth
 * @date 2021/4/23 21:29
 **/
@Data
public class BookWebVo {

    @ApiModelProperty(value = "商品ID")
    private String id;

    @ApiModelProperty(value = "图书一级分类ID")
    private String classifyId;

    @ApiModelProperty(value = "一级分类名称")
    private String classifyLevelOne;

    @ApiModelProperty(value = "二级分类名称")
    private String classifyLevelTwo;

    @ApiModelProperty(value = "图书专业父级ID")
    private String classifyParentId;

    @ApiModelProperty(value = "书名")
    private String title;

    @ApiModelProperty(value = "图书价格")
    private BigDecimal price;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "库存")
    private Long reverse;

    @ApiModelProperty(value = "描述")
    private String description;

}
