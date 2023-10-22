package org.banbang.be.controller.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.banbang.be.pojo.*;
import org.banbang.be.event.EventProducer;
import org.banbang.be.service.CommentService;
import org.banbang.be.service.DiscussPostService;
import org.banbang.be.service.LikeService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.RedisKeyUtil;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.util.*;

/**
 * 帖子
 */
@Controller
@ApiIgnore
@Api(tags = "通知（讨论）页面")
@RequestMapping("discuss")
public class DiscussPostController {

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

    /**
     * 进入帖子发布页
     *
     * @return
     */
    @GetMapping("publish")
    @ApiOperation("进入讨论发布页")
    public String getPublishPage() {
        return "site/discuss-publish";
    }

    /**
     * markdown 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("uploadMdPic")
    @ResponseBody
    @ApiOperation("图片上传")
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
    @PostMapping("add")
    @ResponseBody
    @ApiOperation("添加讨论（发布通知）")
    public String addDiscussPost(@NotEmpty(message = "文章标题不能为空") String title, String content) {
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
                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                .setUserId(user.getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(discussPost.getId());
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, discussPost.getId());

        return BbUtil.getJSONString(201, "发布成功");
    }

    /**
     * 进入帖子详情页
     *
     * @param discussPostId
     * @param model
     * @return
     */
    @GetMapping("detail/{discussPostId}")
    @ApiOperation("进入讨论详情页")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        String content = HtmlUtils.htmlUnescape(discussPost.getContent()); // 内容反转义，不然 markDown 格式无法显示
        discussPost.setContent(content);
        model.addAttribute("post", discussPost);
        // 作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 当前登录用户的点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_POST.value(), discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        // 帖子的评论列表
        List<Comment> commentList = commentService.findCommentByEntity(
                BbEntityType.ENTITY_TYPE_POST.value(), discussPost.getId(), page.getOffset(), page.getLimit());

        // 封装评论及其相关信息
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                // 存储对帖子的评论
                Map<String, Object> commentVo = new HashMap<>();
                commentVo.put("comment", comment); // 评论
                commentVo.put("user", userService.findUserById(comment.getUserId())); // 发布评论的作者
                likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId()); // 该评论点赞数量
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                        hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId()); // 当前登录用户对该评论的点赞状态
                commentVo.put("likeStatus", likeStatus);


                // 存储每个评论对应的回复（不做分页）
                List<Comment> replyList = commentService.findCommentByEntity(
                        BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVoList = new ArrayList<>(); // 封装对评论的评论和评论的作者信息
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply); // 回复
                        replyVo.put("user", userService.findUserById(reply.getUserId())); // 发布该回复的作者
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target); // 该回复的目标用户
                        likeCount = likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), reply.getId());
                        replyVo.put("likeCount", likeCount); // 该回复的点赞数量
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(
                                hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_COMMENT.value(), reply.getId());
                        replyVo.put("likeStatus", likeStatus); // 当前登录用户的点赞状态

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);

                // 每个评论对应的回复数量
                int replyCount = commentService.findCommentCount(BbEntityType.ENTITY_TYPE_COMMENT.value(), comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }

        model.addAttribute("comments", commentVoList);

        return "site/discuss-detail";

    }


    /**
     * 置顶帖子
     *
     * @param id
     * @return
     */
    @PostMapping("top")
    @ResponseBody
    @ApiOperation("置顶讨论")
    public String updateTop(int id, int type) {

        discussPostService.updateType(id, type);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return BbUtil.getJSONString(0);
    }


    /**
     * 加精帖子
     *
     * @param id
     * @return
     */
    @PostMapping("wonderful")
    @ResponseBody
    @ApiOperation("加精讨论")
    public String setWonderful(int id) {

        discussPostService.updateStatus(id, 1);

        // 触发发帖事件，通过消息队列将其存入 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_PUBLISH.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(id);
        eventProducer.fireEvent(event);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScoreKey();
        redisTemplate.opsForSet().add(redisKey, id);

        return BbUtil.getJSONString(0);
    }


    /**
     * 删除帖子
     *
     * @param id
     * @return
     */
    @PostMapping("delete")
    @ResponseBody
    @ApiOperation("删除讨论")
    public String setDelete(int id) {

        discussPostService.updateStatus(id, 2);
        discussPostService.deleteDiscussPost(id);

        // 触发删帖事件，通过消息队列更新 Elasticsearch 服务器
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_DELETE.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(BbEntityType.ENTITY_TYPE_POST.value())
                .setEntityId(id);
        eventProducer.fireEvent(event);

        return BbUtil.getJSONString(0);
    }


}
