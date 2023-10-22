package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户名请求对象")
@Data
public class UsernameRo {
    @ApiModelProperty(value = "用户名", example = "这里是新用户名")
    private String username;
}
