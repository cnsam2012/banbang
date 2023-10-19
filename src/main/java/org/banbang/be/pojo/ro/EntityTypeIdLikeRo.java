package org.banbang.be.pojo.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("点赞实体请求对象")
public class EntityTypeIdLikeRo {
    @ApiModelProperty(value = "实体类型: 1-帖子(entity:post), 2-评论(entity:comment)", example = "1")
    int entityType;

    @ApiModelProperty(value = "实体ID", example = "102")
    int entityId;

    @ApiModelProperty(value = "被赞的实体（帖子/评论）的作者ID", example = "102")
    int entityUserId;

    @ApiModelProperty(value = "帖子的 id (点赞了哪个帖子，点赞的评论属于哪个帖子，点赞的回复属于哪个帖子)", example = "102")
    int postId;
}
