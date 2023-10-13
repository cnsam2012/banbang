package org.banbang.be.ctrler;

import org.banbang.be.pojo.Comment;
import org.banbang.be.pojo.DiscussPost;
import org.banbang.be.pojo.Event;
import org.banbang.be.event.EventProducer;
import org.banbang.be.service.CommentService;
import org.banbang.be.service.DiscussPostService;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.RedisKeyUtil;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * 未改造完成，此处为临时标识
 */
@Deprecated
@ApiIgnore

/**
 * 评论/回复
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加评论
     * @param discussPostId
     * @param comment
     * @return
     */
    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {

        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发评论事件（系统通知）
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_COMMNET.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_POST.value()) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        else if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_COMMENT.value()) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == BbEntityType.ENTITY_TYPE_POST.value()) {
            // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
            event = new Event()
                    .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                    .setUserId(comment.getUserId())
                    .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);

            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
