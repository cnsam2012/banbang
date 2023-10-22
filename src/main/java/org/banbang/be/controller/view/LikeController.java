package org.banbang.be.controller.view;

import org.banbang.be.pojo.Event;
import org.banbang.be.pojo.User;
import org.banbang.be.event.EventProducer;
import org.banbang.be.service.LikeService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.RedisKeyUtil;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * 点赞
 */
@Controller
@ApiIgnore
public class LikeController {

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
     * @param entityType
     * @param entityId
     * @param entityUserId 赞的帖子/评论的作者 id
     * @param postId 帖子的 id (点赞了哪个帖子，点赞的评论属于哪个帖子，点赞的回复属于哪个帖子)
     * @return
     */
    @PostMapping("like")
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId) {
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

        return BbUtil.getJSONString(0, null, map);
    }

}
