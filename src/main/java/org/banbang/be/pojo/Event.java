package org.banbang.be.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装事件（用于系统通知）
 */


@Data
@Accessors(chain = true)
@ApiModel("封装事件（用于系统通知）")
public class Event {

    /**
     * 事件类型
     */
    @ApiModelProperty(value = "事件类型", example = "comment")
    private String topic;

    /**
     * 事件由谁触发
     */
    @ApiModelProperty(value = "事件由谁触发", example = "1")
    private int userId;

    /**
     * 实体类型
     * 帖子 = 1
     * 评论 = 2
     * 人 = 3
     */
    @ApiModelProperty(value = "实体类型", example = "1")
    private int entityType;

    /**
     * 实体 id
     */
    @ApiModelProperty("实体 id")
    private int entityId;

    /**
     * 实体的作者（该通知发送给他）
     */
    @ApiModelProperty("实体的作者（该通知发送给他/该通知的目标/event's destination）")
    private int entityUserId;

    /**
     * 存储未来可能需要用到的数据
     */
    @ApiModelProperty("存储未来可能需要用到的数据")
    private Map<String, Object> data = new HashMap<>();

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
