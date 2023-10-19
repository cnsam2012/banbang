package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("实体请求对象")
public class EntityTypeIdRo {
    @ApiModelProperty(value = "实体类型: 1-帖子(entity:post), 2-评论(entity:comment), 3-用户(entity:user)", example = "3")
    int entityType;

    @ApiModelProperty(value = "实体ID", example = "2")
    int entityId;
}
