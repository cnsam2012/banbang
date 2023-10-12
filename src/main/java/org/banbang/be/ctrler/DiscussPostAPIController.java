package org.banbang.be.ctrler;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.HttpStatus;
import org.banbang.be.event.EventProducer;
import org.banbang.be.pojo.*;
import org.banbang.be.service.CommentService;
import org.banbang.be.service.DiscussPostService;
import org.banbang.be.service.LikeService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbConstant;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.*;

/**
 * 帖子
 */
@RestController
@Api(tags = "通知（讨论）API")
@RequestMapping("/api/discuss")
@Slf4j
public class DiscussPostAPIController implements BbConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    // 网站域名
    @Value("${community.path.domain}")
    private String domain;

    // 项目名(访问路径)
    @Value("${server.servlet.context-path}")
    private String contextPath;

    // editorMd 图片上传地址
    @Value("${community.path.editormdUploadPath}")
    private String editormdUploadPath;

//    /**
//     * 进入帖子发布页
//     *
//     * @return
//     */
//    @GetMapping("/publish")
//    @ApiOperation("进入讨论发布页")
//    public String getPublishPage() {
//        return "/site/discuss-publish";
//    }

    /**
     * markdown 图片上传
     * TODO: 未完善！
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadMdPic")
    @ApiOperation("图片上传 (auth)")
    public String uploadMdPic(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file) {

        String url = null; // 图片访问地址
        try {
            // 获取上传文件的名称
            String trueFileName = file.getOriginalFilename();
            String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
            String fileName = BbUtil.generateUUID() + suffix;

            // 图片存储路径
            File dest = new File(editormdUploadPath + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            // 保存图片到存储路径
            file.transferTo(dest);

            // 图片访问地址
            url = domain + contextPath + "/editor-md-upload/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            return BbUtil.getEditorMdJSONString(500, "上传失败", url);
        }

        return BbUtil.getEditorMdJSONString(201, "上传成功", url);
    }

    /**
     * 添加帖子（发帖）
     *
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加讨论（发布通知） (auth)")
    public String addDiscussPost(
            @NotEmpty(message = "文章标题不能为空")
            @ApiParam(
                    name = "title",
                    defaultValue = "This is title not null or none.",
                    value = "讨论（通知）标题，这个标题不能为空",
                    required = true
            )
            String title,
            @ApiParam(
                    name = "content",
                    defaultValue = "#HEADLINE\ncontent",
                    value = "通知内容，该内容遵循markdown语法并会以md文件渲染"
            )
            String content
    ) {
        User user = hostHolder.getUser();

        if (user == null) {
            return BbUtil.getJSONString(401, "您还未登录");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());

        return BbUtil.getJSONString(201, "发布成功");
    }

    /**
     * 获取所有讨论
     *
     * @param orderMode 默认是 0--按时间排序，可选 1--按分值排序
     * @param page
     * @param resp
     * @return
     */
    @GetMapping("/detail/all")
    @ApiOperation("获取所有讨论")
    public String getAllDiscussPost(
            @RequestParam(name = "orderMode", defaultValue = "0")
            @ApiParam(defaultValue = "0", name = "orderMode", value = "默认是 0--按时间排序，可选 1--按分值排序", example = "0")
            int orderMode,
            Page page,
            HttpServletResponse resp
    ) {

        Map<String, Object> data = new HashMap<>();

        // 获取总页数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index?orderMode=" + orderMode);

        // 分页查询
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(), orderMode);

        // 封装帖子和该帖子对应的用户信息
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);

                User user = userService.findUserById(post.getUserId());
                map.put("user", user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);

                discussPosts.add(map);
            }
        }

        data.put("discussPosts", discussPosts);
        data.put("orderMode", orderMode);
        data.put("page", page);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
        return BbUtil.getJSONString(status, "success", data);
    }

    /**
     * 进入特定讨论详情页
     *
     * @param discussPostId 单条讨论的ID
     * @param page          分页信息
     * @return
     */
    @GetMapping("/detail/{discussPostId}")
    @ApiOperation("进入特定讨论详情页")
    public String getDiscussPost(
            @PathVariable("discussPostId")
            @ApiParam(defaultValue = "3", name = "discussPostId", value = "单条讨论的ID", example = "3")
            int discussPostId,
            Page page
    ) {

        Map<String, Object> data = new HashMap<>();

        // 帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        String content = HtmlUtils.htmlUnescape(discussPost.getContent()); // 内容反转义，不然 markDown 格式无法显示
        discussPost.setContent(content);
        data.put("post", discussPost);

        // 作者
        User user = userService.findUserById(discussPost.getUserId());
        data.put("user", user);

        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        data.put("likeCount", likeCount);

        // 当前登录用户的点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        data.put("likeStatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 帖子的评论列表
        List<Comment> commentList = commentService.findCommentByEntity(
                ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());

        // 封装评论及其相关信息
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 存储对帖子的评论
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment); // 评论
                commentVo.put("user", userService.findUserById(comment.getUserId())); // 发布评论的作者
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId()); // 该评论点赞数量
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                        hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId()); // 当前登录用户对该评论的点赞状态
                commentVo.put("likeStatus", likeStatus);


                // 存储每个评论对应的回复（不做分页）
                List<Comment> replyList = commentService.findCommentByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>(); // 封装对评论的评论和评论的作者信息
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply); // 回复
                        replyVo.put("user", userService.findUserById(reply.getUserId())); // 发布该回复的作者
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target); // 该回复的目标用户
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount); // 该回复的点赞数量
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                                hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus", likeStatus); // 当前登录用户的点赞状态

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 每个评论对应的回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        data.put("comments", commentVoList);
        data.put("page", page);

        var status = HttpStatus.SC_OK;
        return BbUtil.getJSONString(status, "success", data);

    }


    /**
     * 置顶帖子
     *
     * @param id
     * @return
     */
    @PutMapping("/top")
    @ApiOperation("置顶讨论 (auth master-2 only)")
    public String updateTop(
            @ApiParam(required = true)
            int id,
            @ApiParam(required = true)
            int type
    ) {

        discussPostService.updateType(id, type);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        var status = HttpStatus.SC_OK;
        return BbUtil.getJSONString(status, "置顶成功");
    }


    /**
     * 加精帖子
     *
     * @param id
     * @return
     */
    @PutMapping("/wonderful")
    @ApiOperation("加精讨论 (auth master-2 only)")
    public String setWonderful(@ApiParam(required = true) int id) {

        discussPostService.updateStatus(id, 1);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        var status = HttpStatus.SC_OK;
        return BbUtil.getJSONString(status, "加精成功");
    }


    /**
     * 删除帖子
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除讨论 (auth admin-1 only)")
    public String setDelete(@ApiParam(required = true, example = "3") Integer id, HttpServletResponse resp) {

        if (id == null) {
            var status = HttpStatus.SC_EXPECTATION_FAILED;
            resp.setStatus(status);
            return BbUtil.getJSONString(status, "未接收到讨论的ID");
        }

        discussPostService.updateStatus(id, 2);
        discussPostService.deleteDiscussPost(id);

        // 触发删帖事件，通过消息队列更新 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(TOPIC_DELETE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(id);
        eventProducer.fireEvent(event);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
        return BbUtil.getJSONString(status, "删除成功！");
    }


}
