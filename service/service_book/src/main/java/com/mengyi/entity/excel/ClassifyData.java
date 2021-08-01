package com.mengyi.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/4/23 15:16
 **/
@Data
public class ClassifyData {
    @ExcelProperty( index = 0)
    private String oneClassifyName;

    @ExcelProperty( index = 1)
    private String twoClassifyName;
}
