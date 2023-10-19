package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 讨论贴
 *
 */
@Data
@ApiModel("讨论标题、内容请求对象")
public class DiscussPostTitleContentRo {
    @ApiModelProperty("标题，非空")
    private String title;
    @ApiModelProperty("内容")
    private String content;
}
