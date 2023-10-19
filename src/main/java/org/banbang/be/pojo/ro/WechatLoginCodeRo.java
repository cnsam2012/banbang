package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("微信小程序登录请求对象")
@Data
public class WechatLoginCodeRo {
    @ApiModelProperty("临时登录凭证code")
    public String code;
}
