package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("讨论ID、状态请求对象")
@Data
public class DiscussPostIdTypeRo {
    @ApiModelProperty(value = "帖子的id", example = "20")
    private int id;
    @ApiModelProperty(value = "状态：0-普通，1-置顶", example = "0")
    private int type;
}
