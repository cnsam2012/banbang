package org.banbang.be.controller.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.apache.http.HttpStatus;
import org.banbang.be.pojo.DiscussPost;
import org.banbang.be.pojo.Page;
import org.banbang.be.service.ElasticsearchService;
import org.banbang.be.service.LikeService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.R;
import org.banbang.be.util.constant.BbEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索
 */
@Api(tags = "搜索API")
@RestController
public class SearchApiController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * 搜索：根据关键词搜索
     *
     * @param keyword
     * @param page
     * @param resp
     * @return
     */
    @ApiOperation(value = "根据关键词搜索", httpMethod = "GET")
    @GetMapping("api/search")
    @ResponseBody
    public R search(
            @RequestParam("keyword")
            @ApiParam(name = "keyword", value = "关键词", defaultValue = "通知")
            String keyword,
            Page page,
            HttpServletResponse resp
    ) {
        Map<String, Object> data = new HashMap<>();
        if (page.getLimit() == 0 || ObjectUtil.isEmpty(page.getLimit())) {
            page.setLimit(5);
        }

        // 搜索帖子 (Spring 提供的 Page 当前页码从 0 开始计数)
        org.springframework.data.domain.Page<DiscussPost> searchResult =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());

        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null) {
            for (DiscussPost post : searchResult) {
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞数量
                map.put("likeCount", likeService.findEntityLikeCount(BbEntityType.ENTITY_TYPE_POST.value(), post.getId()));

                discussPosts.add(map);
            }
        }

        data.put("discussPosts", discussPosts);
        data.put("keyword", keyword);

        // 设置分页
        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchResult == null ? 0 : (int) searchResult.getTotalElements());
        data.put("page", page);

        var status = HttpStatus.SC_OK;
        resp.setStatus(status);
//        return BbUtil.getJSONString(status, "success", data);
        return R.ok(status, "搜索", data);
    }


}
