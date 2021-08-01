package com.mengyi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/4/14 21:21
 **/
@ApiModel(value = "Teacher查询对象", description = "讲师查询对象封装")
@Data
public class OrderGoodsQuery {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id,模糊查询")
    private String orderNo;

    @ApiModelProperty(value = "订单状态")
    private String status;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "创建时间")
    private String beginTime;

    @ApiModelProperty(value = "处理时间")
    private String solveTime;





}
