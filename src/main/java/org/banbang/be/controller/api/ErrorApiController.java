package org.banbang.be.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
import org.banbang.be.util.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController("api/_err0r")
@Api("异常")
public class ErrorApiController {
    @GetMapping("noLogin")
    @ApiOperation("尚未登录")
    public R errorNoLogin(HttpServletResponse resp) {
        return R.error(resp, HttpStatus.SC_FORBIDDEN, "您尚未登录");
    }
}
