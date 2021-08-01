package com.mengyi.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mengyiyouth
 * @date 2021/1/31 21:08
 **/
@Data
public class CourseQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名称,模糊查询")
    private String title;

    @ApiModelProperty(value = "课程状态")
    private String status;

//    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
//    private String begin;//注意，这里使用的是String类型，前端传过来的数据无需进行类型转换
//
//    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
//    private String end;
}
