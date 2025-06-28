package com.xxxx.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.xxxx.crm.interceptor.NoLoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/28 15:01
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private NoLoginInterceptor noLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(noLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/index", "/css/**", "/js/**", "/lib/**", "/images/**");
    }
}
