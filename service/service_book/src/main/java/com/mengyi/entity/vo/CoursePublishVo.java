package com.mengyi.entity.vo;

import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/1/31 19:28
 **/
@Data
public class CoursePublishVo {
    private static final long serialVersionUID = 1L;

    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectLevelOne;
    private String subjectLevelTwo;
    private String teacherName;
    private String price;//只用于显示
}
