package org.banbang.be.config;

import org.banbang.be.controller.interceptor.CheckLoginInterceptor;
import org.banbang.be.controller.interceptor.DataInterceptor;
import org.banbang.be.controller.interceptor.LoginTicketInterceptor;
import org.banbang.be.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    @Autowired
    private CheckLoginInterceptor checkLoginInterceptor;

    // 对除静态资源外所有路径进行拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/api/user/setting",
                        "/api/user/upload",
                        "/api/discuss/add",
                        "/api/discuss/publish",
                        "/api/comment/add/**",
                        "/api/letter/**",
                        "/api/notice/**",
                        "/api/like",
                        "/api/follow",
                        "/api/unfollow",
                        "/api/discuss/delete",
                        "/api/discuss/delete/",
                        "/api/data/**",
                        "/api/discuss/top",
                        "/api/discuss/wonderful"
                );

        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/css/**", "/js/**", "/img/**", "/editor-md/**", "/editor-md-upload/**");
    }

    // 配置虚拟路径映射访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // System.getProperty("user.dir") 获取程序的当前路径
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\editor-md-upload\\";
        registry.addResourceHandler("/editor-md-upload/**").addResourceLocations("file:" + path);

        // **放行swagger，上线后取消
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // **放行swagger拦截，上线后取消
    }

    // 跨域访问配置，危险
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                .maxAge(3600);
//    }
}
