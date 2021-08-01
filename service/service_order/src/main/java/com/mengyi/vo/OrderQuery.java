package com.mengyi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/4/12 18:50
 **/
@ApiModel(value = "Order查询对象", description = "订单查询对象封装")
@Data
public class OrderQuery {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "订单id,模糊查询")
    private String orderId;

    @ApiModelProperty(value = "图书名称,模糊查询")
    private String title;

    @ApiModelProperty(value = "收货人")
    private String nickName;

    @ApiModelProperty(value = "订单状态")
    private String status;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
}
