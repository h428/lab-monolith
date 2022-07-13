package com.lab.config;

import com.lab.web.filter.RequestBodyCopyFilter;
import com.lab.web.interceptor.LoginInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置
 *
 * @author hao
 */
@SpringBootConfiguration
@ServletComponentScan(basePackages = {"com.lab.web.filter"})
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(loginInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/open/**", "/error"); // 所有无需拦截的 url 以 open 开头
    }

//    @Bean
    public FilterRegistrationBean<RequestBodyCopyFilter> requestBodyFilter(){
        FilterRegistrationBean<RequestBodyCopyFilter> bean = new FilterRegistrationBean<>();
        bean.setOrder(1);
        bean.setFilter(new RequestBodyCopyFilter());
        // 匹配 /system/ 下面的所有url
        bean.addUrlPatterns("/system/*");
        return bean;
    }

}
