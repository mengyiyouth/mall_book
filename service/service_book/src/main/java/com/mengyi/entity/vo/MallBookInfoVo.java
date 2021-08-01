package com.mengyi.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mengyiyouth
 * @date 2021/4/18 16:54
 **/
@Data
public class MallBookInfoVo {

    @ApiModelProperty(value = "商品ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "图书一级分类ID")
    private String classifyId;

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

    @ApiModelProperty(value = "库存")
    private Long reverse;

    @ApiModelProperty(value = "图书状态 Draft未发布  Normal已发布")
    private String status;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "图书简介")
    private String description;

}
