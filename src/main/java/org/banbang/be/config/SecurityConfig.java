package org.banbang.be.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.banbang.be.filter.LoginFilter;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.HostHolder;
import org.banbang.be.util.constant.BbUserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFilter loginFilter;

    /**
     * 静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    // 认证环节我们使用自己的代码 LoginController，绕过 Spring Security 的
    /**
     * 授权
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * 解决与LoginTicketInterceptor冲突的问题
         * 详见 https://zhuanlan.zhihu.com/p/373292177
         */
        http.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class);


        http.authorizeRequests()
                .antMatchers(
                        "/user/setting",
                        "/user/upload",
                        "/discuss/add",
                        "/discuss/publish",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow",

                        "/api/user/setting",
                        "/api/user/upload",
                        "/api/discuss/add",
                        "/api/discuss/publish",
                        "/api/comment/add/**",
                        "/api/letter/**",
                        "/api/notice/**",
                        "/api/like",
                        "/api/follow",
                        "/api/unfollow"
                )
                .hasAnyAuthority(
                        BbUserAuth.AUTHORITY_USER.value(),
                        BbUserAuth.AUTHORITY_ADMIN.value(),
                        BbUserAuth.AUTHORITY_MODERATOR.value()
                )

                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful",

                        "/api/discuss/top",
                        "/api/discuss/wonderful"
                )
                .hasAnyAuthority(
                        BbUserAuth.AUTHORITY_ADMIN.value(),
                        BbUserAuth.AUTHORITY_MODERATOR.value()
                )

                .antMatchers(
                        "/discuss/top",
                        "/discuss/wonderful",

                        "/api/discuss/top",
                        "/api/discuss/wonderful",

                        "/discuss/delete",
                        "/discuss/delete/",
                        "/data/**",

                        "/api/discuss/delete",
                        "/api/discuss/delete/",
                        "/api/data/**"
                )
                .hasAnyAuthority(
                        BbUserAuth.AUTHORITY_ADMIN.value()
                )

                .anyRequest().permitAll()

                .and().csrf().disable();

        // 权限不够时的处理
        http.exceptionHandling()

                // 1. 未登录时的处理
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");

//                        if ("XMLHttpRequest".equals(xRequestedWith)) {
//                            // 异步请求
//                        response.setContentType("application/plain;charset=utf-8");
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        response.setStatus(HttpStatus.SC_FORBIDDEN);
                        writer.write(BbUtil.getJSONString(403, "你还没有登录"));
//                        } else {
//                            // 普通请求
//                            response.sendRedirect(request.getContextPath() + "/login");
//                        }

                    }
                })

                // 2. 权限不够时的处理
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("x-requested-with");

//                        if ("XMLHttpRequest".equals(xRequestedWith)) {
//                            // 异步请求
//                        response.setContentType("application/plain;charset=utf-8");
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        response.setStatus(HttpStatus.SC_FORBIDDEN);
                        writer.write(BbUtil.getJSONString(403, "你没有访问该功能的权限"));
//                        } else {
//                            // 普通请求
//                            response.sendRedirect(request.getContextPath() + "/denied");
//                        }

                    }
                });

        // Security 底层会默认拦截 /logout 请求，进行退出处理
        // 此处赋予它一个根本不存在的退出路径，使得程序能够执行到我们自己编写的退出代码
        http.logout().logoutUrl("/securitylogout");

        http.headers().frameOptions().sameOrigin();
    }
}
