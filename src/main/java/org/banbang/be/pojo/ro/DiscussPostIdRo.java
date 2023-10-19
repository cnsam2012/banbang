package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("讨论ID请求对象")
@Data
public class DiscussPostIdRo {
    @ApiModelProperty(value = "帖子的id", example = "20")
    private int id;
}
