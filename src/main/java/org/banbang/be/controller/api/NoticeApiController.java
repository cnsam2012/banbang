package org.banbang.be.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.banbang.be.pojo.User;
import org.banbang.be.service.MessageService;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;


@Api(tags = "通知（未读信息）")
@RestController
public class NoticeApiController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private MessageService messageService;

    @GetMapping("api/notice/unread")
    @ApiOperation("获取未读私信/系统通知的数量")
    public R getAllUnreadNotice(
            HttpServletResponse response
    ) {
        User user = hostHolder.getUser();
        var data = new HashMap<String, Object>();
        if (user != null) {
            int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
            int noticeUnreadCount = messageService.findNoticeUnReadCount(user.getId(), null);
            data.put("allUnreadCount", letterUnreadCount + noticeUnreadCount);
            return R.ok(response, "未读信息", data);
        }
        return R.error(response, "no_logging_user");
    }
}
