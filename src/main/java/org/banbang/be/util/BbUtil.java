package org.banbang.be.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.banbang.be.pojo.LoginTicket;
import org.banbang.be.pojo.User;
import org.banbang.be.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class BbUtil {

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String generateUUID() {
        // 去除 ”-“
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * md5 加密
     *
     * @param key 要加密的字符串
     * @return
     */
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 将服务端返回的消息封装成 JSON 格式的字符串
     *
     * @param code    状态码
     * @param msg     提示消息
     * @param dataMap 业务数据
     * @return 返回 JSON 格式字符串
     */
    public static String getJSONString(int code, String msg, Map<String, Object> dataMap) {
        JSONObject json = new JSONObject();

        json.put("code", code);
        json.put("msg", msg);

        if (dataMap != null) {
            for (String key : dataMap.keySet()) {
                json.put(key, dataMap.get(key));
            }
        }
        return json.toJSONString();
    }


    // 重载 getJSONString 方法，服务端方法可能不返回业务数据
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    // 重载 getJSONString 方法，服务端方法可能不返回业务数据和提示消息
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    // editor.md 要求返回的 JSON 字符串格式
    public static String getEditorMdJSONString(int success, String message, String url) {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", message);
        json.put("url", url);
        return json.toJSONString();
    }

    /**
     * 生成指定位数的数字随机数, 最高不超过 9 位
     *
     * @param length
     * @return
     */
    public static String getRandomCode(int length) {
        Validate.isTrue(length <= 9 && length > 0, "生成数字随机数长度范围应该在 1~9 内, 参数 length : %s", length);
        int floor = (int) Math.pow(10, length - 1);
        int codeNum = RandomUtils.nextInt(floor, floor * 10);
        return Integer.toString(codeNum);
    }

    public static void setContext(String ticket, UserService userService, HostHolder hostHolder) {
        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证状态（是否有效）以及是否过期
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户信息
                hostHolder.setUser(user);
                // 构建用户认证的结果，并存入 SecurityContext, 以便于 Spring Security 进行授权
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(
                                user, user.getPassword(), userService.getAuthorities(user.getId())
                        );
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
    }

    public static Map<String, Object> checkTicketFromHeaderAndGetUserInfo(HttpServletRequest request, UserService userService) {
        HttpServletRequest req = (HttpServletRequest) request;
        String ticket = null;
        ticket = req.getHeader("ticket");
        log.info("ticket {}", ticket);
        var res = new HashMap<String, Object>();
        User user = null;
        if (ObjectUtil.isEmpty(ticket)) {
            res.put("errMsg", "请求中无token/ticket");
            return res;
        }
        // 查询凭证
        LoginTicket loginTicket = userService.findLoginTicket(ticket);
        log.info("loginTicket {}", loginTicket);
        if (loginTicket == null || loginTicket.getStatus() != 0) {
            res.put("errMsg", "查无token/ticket，或已退出");
            return res;
        }

        if (!loginTicket.getExpired().after(new Date())) {
            res.put("errMsg", "token/ticket过期");
            return res;
        }
        user = userService.findUserById(loginTicket.getUserId());
        log.info("user {}", user);
        if (user == null) {
            res.put("errMsg", "查无此人");
            return res;
        }
        res.put("expired", loginTicket.getExpired());
        res.put("userId", loginTicket.getUserId());
        res.put("user", user);
        return res;
    }


    /**
     * 测试
     */
    @Test
    public void testBbUtil() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jack");
        map.put("age", 18);

        log.info(getJSONString(0, "ok", map));
        log.info(BbUtil.generateUUID());

        Assertions.assertEquals(1, 1);
    }

}
