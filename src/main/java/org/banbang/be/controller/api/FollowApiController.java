package org.banbang.be.controller.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.banbang.be.event.EventProducer;
import org.banbang.be.pojo.Event;
import org.banbang.be.pojo.Page;
import org.banbang.be.pojo.User;
import org.banbang.be.pojo.ro.EntityTypeIdRo;
import org.banbang.be.service.FollowService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.R;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关注(目前只做了关注用户)
 */
@RestController
@Api(tags = "关注(目前只做了关注用户)")
public class FollowApiController {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;

    /**
     * 关注
     *
     * @param fer
     * @param resp
     * @return
     */
    @PutMapping("/api/follow")
    @ApiOperation("关注(目前只做了关注用户)")
    public R follow(
            @RequestBody
            EntityTypeIdRo fer,
            HttpServletResponse resp
    ) {
        var entityType = fer.getEntityType();
        entityType = 3;
        var entityId = fer.getEntityId();
        User user = hostHolder.getUser();
        followService.follow(user.getId(), entityType, entityId);

        // 触发关注事件（系统通知）
        Event event = new Event()
                .setTopic(BbKafkaTopic.TOPIC_FOLLOW.value())
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

//        return BbUtil.getJSONString(status, "已关注");
        return R.ok(resp, "已关注");
    }

    /**
     * 取消关注
     *
     * @param etir
     * @return
     */
    @PutMapping("/api/unfollow")
    @ApiOperation("取消关注(用户)")
    public R unfollow(
            @RequestBody
            @ApiParam(required = true)
            EntityTypeIdRo etir,
            HttpServletResponse resp
    ) {
        var entityType = etir.getEntityType();
        entityType = 3;
        var entityId = etir.getEntityId();

        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

//        return BbUtil.getJSONString(0, "已取消关注");
        return R.ok(resp, "已取消关注");

    }


    /**
     * 某个用户的关注列表（人）
     * @param userId
     * @param page
     * @param resp
     * @return
     */
    @GetMapping("/api/followees/{userId}")
    @ApiOperation("某个用户的关注列表（人）")
    public R getFollowees(
            @PathVariable("userId")
            @ApiParam(required = true)
            int userId,
            Page page,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        data.put("user", user);

        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, BbEntityType.ENTITY_TYPE_USER.value()));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());

        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user"); // 被关注的用户
                map.put("hasFollowed", hasFollowed(u.getId())); // 判断当前登录用户是否已关注这个关注列表中的某个用户
            }
        }

        data.put("followees", userList);

//        return "/site/followee";
        return R.ok(resp, "关注列表（人）", data);
    }

    /**
     * 某个用户的粉丝列表
     * @param userId
     * @param page
     * @param resp
     * @return
     */
    @GetMapping("/api/fans/{userId}")
    @ApiOperation("某个用户的粉丝列表")
    public R getFans(
            @PathVariable("userId") int userId,
            Page page,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();

        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        data.put("user", user);

        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }
        page.setPath("/fans/" + userId);
        page.setRows((int) followService.findFollowerCount(BbEntityType.ENTITY_TYPE_USER.value(), userId));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());

        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user"); // 被关注的用户
                map.put("hasFollowed", hasFollowed(u.getId())); // 判断当前登录用户是否已关注这个关注列表中的某个用户
            }
        }

        data.put("fans", userList);

//        return "/site/follower";
        return R.ok(resp, "用户的粉丝列表", data);
    }

    /**
     * 判断当前登录用户是否已关注某个用户
     *
     * @param userId 某个用户
     * @return
     */
    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), BbEntityType.ENTITY_TYPE_USER.value(), userId);
    }


}
