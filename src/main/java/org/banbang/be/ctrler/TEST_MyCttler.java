package org.banbang.be.ctrler;

import org.banbang.be.pojo.DiscussPost;
import org.banbang.be.pojo.Page;
import org.banbang.be.pojo.User;
import org.banbang.be.service.DiscussPostService;
import org.banbang.be.service.LikeService;
import org.banbang.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.banbang.be.util.BbConstant.ENTITY_TYPE_POST;

@Controller
@ResponseBody
public class TEST_MyCttler {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/bbt")
    public String getIndexPage(Model model, Page page, @RequestParam(name = "orderMode", defaultValue = "0") int orderMode) {
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
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        return "index";
    }

    @RequestMapping(value = "bl", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String foftest(Integer ok) throws Exception {
        if (ok == null) {
            return "null";
        } else {
            return ok.toString();
        }
    }


}
