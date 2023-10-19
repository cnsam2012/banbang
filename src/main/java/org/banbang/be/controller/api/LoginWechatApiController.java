package org.banbang.be.controller.api;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.var;
import org.apache.http.HttpStatus;
import org.banbang.be.pojo.ro.WechatLoginCodeRo;
import org.banbang.be.pojo.vo.WeUserInfoVo;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Api(tags = "微信applet登录接口")
@RestController
@RequestMapping("/api/wechat")
public class LoginWechatApiController {
    @Autowired
    private UserService userService;

//    @Autowired
//    private Producer kaptchaProducer;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Value("${server.servlet.context-path}")
//    private String contextPath;

    /**
     * 微信一键登录接口
     *
     * @param wlcr
     * @param resp
     * @return
     */
    @PostMapping("/getUserInfoAndLogin")
    @ApiOperation("微信一键登录接口")
    public R getUserLoginByApplets(
            @RequestBody
            @ApiParam(required = true)
            WechatLoginCodeRo wlcr,
            HttpServletResponse resp
    ) {
        var data = new HashMap<String, Object>();
        var status = HttpStatus.SC_OK;
        var isSuccess = true;
        var msg = "登录/注册成功";
        var tempLoginCode = wlcr.getCode();

        WeUserInfoVo weUserInfoVo = userService.loginByWeApplets(tempLoginCode);

        if (weUserInfoVo.getId() < 0) {
            status = HttpStatus.SC_BAD_REQUEST;
            isSuccess = false;
            msg = "登录/注册失败";
        }

        data.put("weUserInfo", weUserInfoVo);
//        return BbUtil.getJSONString(status, msg, data);
        return R.definition(resp, status, msg, isSuccess, data);
    }

    /**
     * 获取用户信息与登录状态
     *
     * @param req
     * @param resp
     * @return
     */
    @PostMapping("/checkLoginStatus")
    @ApiOperation("获取用户信息与登录状态")
    public R getUserInfoWithTicketByApplets(
            HttpServletRequest req,
            HttpServletResponse resp
    ) {
        var result = BbUtil.checkTicketFromHeaderAndGetUserInfo(req, userService);
        if (!ObjectUtil.isEmpty(result.get("errMsg"))) {
            return R.error(resp, HttpStatus.SC_BAD_REQUEST, "ticket异常", result);
        }
        return R.ok(resp, "用户信息与登录状态", result);
    }
}
