package org.banbang.be.controller.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.banbang.be.pojo.User;
import org.banbang.be.service.UserService;
import org.banbang.be.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Deprecated
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 在 Controller 执行之前被调用
     * 检查凭证状态，若凭证有效则在本次请求中持有该用户信息
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从 cookie 中获取凭证
//        String ticket = null;
//        try {
//            ticket = CookieUtil.getValue(request, "ticket");
//            log.warn("在cookie中取得ticket: {}", ticket);
//        } catch (Exception e) {
//            log.error(e.toString());
//        }
//        if (ticket != null) {
//            // 查询凭证
//            LoginTicket loginTicket = userService.findLoginTicket(ticket);
//            // 检查凭证状态（是否有效）以及是否过期
//            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
//                // 根据凭证查询用户
//                User user = userService.findUserById(loginTicket.getUserId());
//                // 在本次请求中持有用户信息
//                hostHolder.setUser(user);
//                // 构建用户认证的结果，并存入 SecurityContext, 以便于 Spring Security 进行授权
//                Authentication authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                user, user.getPassword(), userService.getAuthorities(user.getId())
//                        );
//                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
//            }
//        }
        return true;
    }

    /**
     * 在模板引擎之前被调用
     * 将用户信息存入 modelAndView, 便于模板引擎调用
     *
     * @param request
     * @param response
     * @param handler
                * @param modelAndView
                * @throws Exception
                */
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            User user = hostHolder.getUser();
            log.info("user in holder got! -- {}", user);
            if (user != null && modelAndView != null) {
                modelAndView.addObject("loginUser", user);
            }
    }

    /**
     * 在 Controller 执行之后（即服务端对本次请求做出响应后）被调用
     * 清理本次请求持有的用户信息
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }


}
