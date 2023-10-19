package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("发送私信：目标与内容请求对象")
public class SendLetter2NameContentRo {
    @ApiModelProperty(value = "私信目标", example = "admin")
    private String toName;

    @ApiModelProperty(value = "私信内容", example = "这是一条发给用户admin的测试信息")
    private String content;
}
