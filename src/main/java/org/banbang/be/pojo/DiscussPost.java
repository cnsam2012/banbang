package org.banbang.be.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 讨论贴
 * 对应数据库表 `discuss_post`
 */
@Data
@Document(indexName = "discusspost", type = "_doc", shards = 6, replicas = 3)
@ApiModel("讨论贴")
public class DiscussPost {

    /**
     * 唯一标识
     */
    @Id
    @ApiModelProperty(value = "唯一标识", example = "3")
    private int id;

    /**
     * 发布讨论的作
     */
    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "发布讨论的作者", example = "102")
    private int userId;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @ApiModelProperty(value = "标题", example = "这个是不为空的标题")
    private String title;

    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    @ApiModelProperty(value = "内容", example = "#### Welcome to")
    private String content;

    /**
     * 置顶类型：0-普通 1-置顶
     */
    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "置顶类型：0-普通 1-置顶", example = "0")
    private int type;

    /**
     * 状态：0-正常，1-精华 2-拉黑
     */
    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "状态：0-正常，1-精华 2-拉黑", example = "0")
    private int status;

    /**
     * 发布时间
     */
    @Field(type = FieldType.Date)
    @ApiModelProperty(value = "发布时间", example = "2023-09-24 17:12:17")
    private Date createTime;

    /**
     * 评论数量
     */
    @Field(type = FieldType.Integer)
    @ApiModelProperty(value = "评论数量", example = "0")
    private int commentCount;

    /**
     * 热度/分数
     */
    @Field(type = FieldType.Double)
    @ApiModelProperty(value = "热度/分数", example = "3553")
    private double score;

    /**
     * 逻辑删除
     * optional: MP automatically convert "isDeleted" into "is_deleted"
     * 可选：Mybatis-Plus 自动转换驼峰命名
     * `@TableField(value = "is_deleted")`
     */
    @TableLogic
    @ApiModelProperty(value = "逻辑删除", example = "0")
    private int isDeleted;

}
