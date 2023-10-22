package org.banbang.be.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.apache.http.HttpStatus;
import org.banbang.be.event.EventProducer;
import org.banbang.be.pojo.Event;
import org.banbang.be.pojo.User;
import org.banbang.be.pojo.ro.EntityTypeIdLikeRo;
import org.banbang.be.service.LikeService;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.R;
import org.banbang.be.util.RedisKeyUtil;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞
 */
@Controller
@Api(tags = "点赞")
public class LikeApiController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param elr
     * @param resp
     * @return
     */
    @ApiOperation("更改点赞状态，1:已赞，0:未赞，重复请求以取消/重新点赞")
    @PostMapping("api/like")
    @ResponseBody
    public R like(
            @RequestBody
            EntityTypeIdLikeRo elr,
            HttpServletResponse resp
    ) {

        // 取参：实体类型，实体id，被赞作者id，被赞评论所在帖子id
        var entityType = elr.getEntityType();
        var entityId = elr.getEntityId();
        var entityUserId = elr.getEntityUserId();
        var postId = elr.getPostId();

        User user = hostHolder.getUser();
        // 点赞
        likeService.like(user.getId(), entityType, entityId, entityUserId);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件（系统通知） - 取消点赞不通知
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(BbKafkaTopic.TOPIC_LIKE.value())
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            eventProducer.fireEvent(event);
        }

        if (entityType == BbEntityType.ENTITY_TYPE_POST.value()) {
            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, postId);
        }

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "like_status_changed", map);

        if (likeStatus == 1) { // 1:已赞，0:未赞
            return R.ok(status,"已点赞",map);
        } else {
            return R.ok(status,"取消点赞",map);
        }

    }

}
