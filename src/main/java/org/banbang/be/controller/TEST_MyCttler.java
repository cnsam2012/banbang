package org.banbang.be.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.apache.http.HttpStatus;
import org.banbang.be.pojo.ro.WechatLoginCodeRo;
import org.banbang.be.pojo.vo.WeUserInfoVo;
import org.banbang.be.service.DiscussPostService;
import org.banbang.be.service.LikeService;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Controller
@ResponseBody
@Api(hidden = true)
public class TEST_MyCttler {
//
//    @Autowired
//    private DiscussPostService discussPostService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private LikeService likeService;
//
//    @RequestMapping(value = "bl", method = {RequestMethod.POST})
//    @ResponseBody
//    @ApiOperation("一个测试控制器")
//    public String foftest(
//            @RequestBody
//            @ApiParam(required = true)
//            WechatLoginCodeRo wlcr,
//            HttpServletResponse resp
//    ) {
//        var data = new HashMap<String, Object>();
//        var status = HttpStatus.SC_OK;
//        var msg = "login_or_reg_success";
//        var tempLoginCode = wlcr.getCode();
//
//        WeUserInfoVo weUserInfoVo = userService.loginByWeApplets(tempLoginCode);
//
//        if (weUserInfoVo.getId() < 0) {
//            status = HttpStatus.SC_BAD_REQUEST;
//            msg = "login_or_reg_failed";
//        }
//
//        data.put("weUserInfo", weUserInfoVo);
//        resp.setStatus(status);
//        return BbUtil.getJSONString(status, msg, data);
//    }
}
