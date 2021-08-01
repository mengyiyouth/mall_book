package com.mengyi.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.mengyi.entity.OrderGoods;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author mengyiyouth
 * @date 2021/4/16 16:32
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order")
@ApiModel(value="TOrder对象", description="订单")
public class AllOrdersAndGoods {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;


    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "退货处理状态")
    private Integer status;

    @ApiModelProperty(value = "该订单对应的所有商品信息")
    private List<OrderGoods> allGoodsDetail;

}
