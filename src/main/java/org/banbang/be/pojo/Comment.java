package org.banbang.be.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 评论
 * 对应数据库表 `comment`
 */
@Data
@ApiModel("评论")
public class Comment {

    /**
     * 评论唯一标识
     */
    @ApiModelProperty(value = "唯一标识", example = "3")
    private int id;

    /**
     * 发布评论的作者
     */
    @ApiModelProperty(value = "发布评论的作者", example = "102")
    private int userId;

    /**
     * 评论目标的类型（帖子、评论）
     */
    @ApiModelProperty(value = "评论目标的类型（帖子、评论）", example = "2")
    private int entityType;

    /**
     * 评论目标的ID（帖子ID、评论ID）
     */
    @ApiModelProperty(value = "评论目标的ID（帖子ID、评论ID）", example = "1")
    private int entityId;

    /**
     * 指明对哪个用户进行评论(用户 id)
     */
    @ApiModelProperty(value = "指明对哪个用户进行评论(用户 id)", example = "102")
    private int targetId;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", example = "再回一个")
    private String content;

    /**
     * 状态：0-正常，1-禁用
     */
    @ApiModelProperty(value = "状态：0-正常，1-禁用", example = "0")
    private int status;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间", example = "2023-09-24 17:12:17")
    private Date createTime;

}
