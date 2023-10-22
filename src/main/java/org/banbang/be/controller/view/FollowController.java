package org.banbang.be.controller.view;

import org.banbang.be.pojo.Event;
import org.banbang.be.pojo.Page;
import org.banbang.be.pojo.User;
import org.banbang.be.event.EventProducer;
import org.banbang.be.service.FollowService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.constant.BbEntityType;
import org.banbang.be.util.constant.BbKafkaTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 关注(目前只做了关注用户)
 */
@Controller
@ApiIgnore
public class FollowController {

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
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
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

        return BbUtil.getJSONString(0, "已关注");
    }

    /**
     * 取消关注
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping("unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return BbUtil.getJSONString(0, "已取消关注");
    }

    /**
     * 某个用户的关注列表（人）
     * @param userId
     * @param page
     * @param model
     * @return
     */
    @GetMapping("followees/{userId}")
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
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

        model.addAttribute("users", userList);

        return "site/followee";
    }

    /**
     * 某个用户的粉丝列表
     * @param userId
     * @param page
     * @param model
     * @return
     */
    @GetMapping("followers/{userId}")
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(BbEntityType.ENTITY_TYPE_USER.value(), userId));

        // 获取关注列表
        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());

        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user"); // 被关注的用户
                map.put("hasFollowed", hasFollowed(u.getId())); // 判断当前登录用户是否已关注这个关注列表中的某个用户
            }
        }

        model.addAttribute("users", userList);

        return "site/follower";
    }

    /**
     * 判断当前登录用户是否已关注某个用户
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
