package org.banbang.be.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.banbang.be.service.UserService;
import org.banbang.be.util.BbUtil;
import org.banbang.be.util.CookieUtil;
import org.banbang.be.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器，前置于`UsernamePasswordAuthenticationFilter.class`。
 * 详见 `org.banbang.be.config.WebSecurityConfigurerAdapter:60`
 */
@Slf4j
@Component
public class LoginFilter implements Filter {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        log.info("requestURI is {}", requestURI);
        String ticket = null;
        // 对于调用api的请求，从header中取token
        if (requestURI.indexOf("/api") > -1) {
            if (ticket == null || StringUtils.isBlank(ticket) || ticket == "null") {
                ticket = request.getHeader("ticket");
                log.info("在header中取得ticket: {}", ticket);
            }

            if (ticket == null || StringUtils.isBlank(ticket) || ticket == "null") {
                ticket = request.getHeader("Ticket");
                log.info("在header中取得Ticket: {}", ticket);
            }
        } else {
            // 其他请求从cookie中取header
            try {
                ticket = CookieUtil.getValue(request, "ticket");
                log.info("在cookie中取得ticket: {}", ticket);
            } catch (Exception e) {
                log.error(e.toString());
            }
        }

        BbUtil.setContext(ticket, userService, hostHolder);

        // 让请求继续向下执行.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
