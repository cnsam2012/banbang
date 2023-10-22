package org.banbang.be.controller.api;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.banbang.be.pojo.User;
import org.banbang.be.pojo.ro.LoginUserRo;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.R;
import org.banbang.be.util.RedisKeyUtil;
import org.banbang.be.util.constant.BbExpiredSeconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录、登出、注册
 */
@Slf4j
@RestController
@Api(tags = "登录、登出、注册、验证码API")
public class LoginApiController {


    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 注册用户
     *
     * @param user
     * @param resp
     * @return
     */
    @PostMapping("api/register")
    public R register(
            @RequestBody
            User user,
            HttpServletResponse resp
    ) {
        Map<String, Object> map = userService.register(user);
        Map<String, Object> data = new HashMap<>();

        if (map == null || map.isEmpty()) {
            data.put("msg", "注册成功, 我们已经向您的邮箱发送了一封激活邮件，请尽快激活!");

            var status = HttpStatus.SC_OK; //200
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "reg_success", data);
            return R.ok(status, "注册成功", data);
        } else {
            data.put("usernameMsg", map.get("usernameMsg"));
            data.put("passwordMsg", map.get("passwordMsg"));
            data.put("emailMsg", map.get("emailMsg"));

            var status = HttpStatus.SC_BAD_REQUEST; //400
            resp.setStatus(status);
//            return BbUtil.getJSONString(status, "reg_fail", data);
            return R.error(status, "注册失败", data);
        }
    }


    /**
     * 生成验证码, 并存入 Redis
     *
     * @param response
     */
    @GetMapping("api/kaptcha")
    public void getKaptcha(
            HttpServletResponse response
    ) {
        // 生成验证码
        String text = kaptchaProducer.createText(); // 生成随机字符

//        System.out.println("验证码：" + text);
        log.info("验证码：" + text);
        BufferedImage image = kaptchaProducer.createImage(text); // 生成图片

        // 验证码的归属者
        String kaptchaOwner = BbUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        log.info("验证码属于：{}", kaptchaOwner);


        // 将验证码存入 redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败", e.getMessage());
        }
    }

    /**
     * 验证用户输入的图片验证码是否和redis中存入的是否相等
     *
     * @param kaptchaOwner 从 cookie 中取出的 kaptchaOwner
     * @param checkCode    用户输入的图片验证码
     * @return 失败则返回原因, 验证成功返回 "",
     */
    private String checkKaptchaCode(
            String kaptchaOwner, String checkCode
    ) {
        if (StringUtils.isBlank(checkCode)) {
            return "未发现输入的图片验证码";
        }
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        String kaptchaValue = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(kaptchaValue)) {
            return "图片验证码过期";
        } else if (!kaptchaValue.equalsIgnoreCase(checkCode)) {
            return "图片验证码错误";
        }
        return "";
    }

    /**
     * 用户登录
     *
     * @param user
     * @param response
     * @param kaptchaOwner
     * @return
     */
    @PostMapping("api/login")
    public R login(
            @ApiParam(required = true)
            @RequestBody
            LoginUserRo user,
            HttpServletResponse response,
            @CookieValue("kaptchaOwner")
            String kaptchaOwner
    ) {
        var username = user.getUsername();
        var password = user.getPassword();
        var code = user.getCode();
        var rememberMe = user.isRememberMe();

        Map<String, Object> data = new HashMap<>();

        // 检查验证码
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            data.put("codeMsg", "验证码错误");
            var status = HttpStatus.SC_BAD_REQUEST;
            response.setStatus(status);
//            return BbUtil.getJSONString(status, "login_failed", data);
            return R.error(status, "登录失败", data);
        }

        // 凭证过期时间（是否记住我）: 7天 & 12小时
        var rem = BbExpiredSeconds.REMEMBER_EXPIRED_SECONDS.value();
        var def = BbExpiredSeconds.DEFAULT_EXPIRED_SECONDS.value();
        int expiredSeconds = rememberMe ? rem : def;

        // 验证用户名和密码
        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        if (map.containsKey("ticket")) {
            // 账号和密码均正确，则服务端会生成 ticket，浏览器通过 cookie 存储 ticket
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath); // cookie 有效范围
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            var status = HttpStatus.SC_OK;
            response.setStatus(status);
//            return BbUtil.getJSONString(status, "login_success");
            return R.ok(status, "登录成功");
        } else {
            data.put("usernameMsg", map.get("usernameMsg"));
            data.put("passwordMsg", map.get("passwordMsg"));
            var status = HttpStatus.SC_BAD_REQUEST;
            response.setStatus(status);
//            return BbUtil.getJSONString(status, "login_failed", data);
            return R.error(status, "登录失败", data);
        }

    }

    /**
     * 用户登出
     *
     * @param ticket 设置凭证状态为无效
     * @return
     */
    @GetMapping("api/logout")
    public R logout(
            @CookieValue("ticket") String ticket
    ) {
        var username = userService.findUserById(userService.findLoginTicket(ticket).getUserId()).getUsername();
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        var status = HttpStatus.SC_OK;
        return R.ok(status, username + " 已退出");
    }

    /**
     * 重置密码
     * TODO 完善参数封装
     */
    @PostMapping("api/resetPwd")
    @ResponseBody
    public R resetPwd(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("emailVerifyCode") String emailVerifyCode,
                      @RequestParam("kaptchaCode") String kaptcha,
                      HttpServletResponse resp,
                      @CookieValue("kaptchaOwner") String kaptchaOwner) {
        Map<String, Object> data = new HashMap<>(4);
        var status = HttpStatus.SC_OK;

        // 检查图片验证码
        String kaptchaCheckRst = checkKaptchaCode(kaptchaOwner, kaptcha);
        if (StringUtils.isNotBlank(kaptchaCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", kaptchaCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "验证码错误", data);
        }

        // 检查邮件验证码
        String emailVerifyCodeCheckRst = checkRedisResetPwdEmailCode(username, emailVerifyCode);
        if (StringUtils.isNotBlank(emailVerifyCodeCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", emailVerifyCodeCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "邮件验证码错误", data);
        }

        // 执行重置密码操作
        Map<String, Object> stringObjectMap = userService.doResetPwd(username, password);
        String usernameMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(usernameMsg)) {
            data.put("status", "0");
            data.put("msg", "重置密码成功!");
            data.put("target", "/login");
            status = HttpStatus.SC_OK;
            resp.setStatus(status);
            return R.ok(status, "密码重置成功", data);
        } else {
            data.put("errMsg", usernameMsg);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "密码重置失败", data);
        }
    }

    /**
     * 发送邮件验证码(用于重置密码)
     * TODO 完善参数封装
     *
     * @param kaptchaOwner 从 cookie 中取出的 kaptchaOwner
     * @param kaptcha      用户输入的图片验证码
     * @param username     用户输入的需要找回的账号
     */
    @PostMapping("api/sendEmailCodeForResetPwd")
    @ResponseBody
    public R sendEmailCodeForResetPwd(
            @CookieValue("kaptchaOwner") String kaptchaOwner,
            @RequestParam("kaptcha") String kaptcha,
            @RequestParam("username") String username,
            HttpServletResponse resp
    ) {
        Map<String, Object> data = new HashMap<>(3);
        var status = HttpStatus.SC_OK;
        // 检查图片验证码
        String kaptchaCheckRst = checkKaptchaCode(kaptchaOwner, kaptcha);
        if (StringUtils.isNotBlank(kaptchaCheckRst)) {
            data.put("status", "1");
            data.put("errMsg", kaptchaCheckRst);
            status = HttpStatus.SC_BAD_REQUEST;
            return R.error(status, "图片验证码不通过", data);
        }
        Map<String, Object> stringObjectMap = userService.doSendEmailCode4ResetPwd(username);
        String usernameMsg = (String) stringObjectMap.get("errMsg");
        if (StringUtils.isBlank(usernameMsg)) {
            data.put("status", "0");
            data.put("msg", "已经往您的邮箱发送了一封验证码邮件, 请查收!");
            status = HttpStatus.SC_OK;
            return R.ok(status, "", data);
        } else {
            data.put("errMsg", usernameMsg);
            status = HttpStatus.SC_BAD_REQUEST;
            resp.setStatus(status);
            return R.error(status, "失败", data);
        }
    }

    /**
     * 检查 邮件 验证码
     *
     * @param username  用户名
     * @param checkCode 用户输入的图片验证码
     * @return 验证成功 返回"", 失败则返回原因
     */
    private String checkRedisResetPwdEmailCode(String username, String checkCode) {
        if (StringUtils.isBlank(checkCode)) {
            return "未发现输入的邮件验证码";
        }
        final String redisKey = "EmailCode4ResetPwd:" + username;
        String emailVerifyCodeInRedis = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isBlank(emailVerifyCodeInRedis)) {
            return "邮件验证码已过期";
        } else if (!emailVerifyCodeInRedis.equalsIgnoreCase(checkCode)) {
            return "邮件验证码错误";
        }
        return "";
    }


}
