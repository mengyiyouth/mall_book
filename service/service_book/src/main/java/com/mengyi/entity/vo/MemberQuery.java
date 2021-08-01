package com.mengyi.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mengyiyouth
 * @date 2021/4/20 19:01
 **/
@Data
@ApiModel(value = "Member查询对象", description = "前台成员查询对象封装")
public class MemberQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "昵称,模糊查询")
    private String nickname;

    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换

    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
