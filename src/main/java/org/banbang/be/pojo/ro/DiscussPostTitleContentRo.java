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
    @ApiModelProperty(value = "标题，非空", example = "not_null_title")
    private String title;
    @ApiModelProperty(value = "内容", example = "content")
    private String content;
}
