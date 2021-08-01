package com.mengyi;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mengyiyouth
 * @date 2021/2/7 21:14
 **/
@Data
public class UcenterMemberPay {
    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
