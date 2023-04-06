package com.any.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        // 1. 添加CORS配置信息
        CorsConfiguration configuration = new CorsConfiguration();
        // 2.允许的域，不要写*，否则cookie就无法使用了，填写亲请求的前端服务器
        configuration.addAllowedOrigin("http://localhost:9000");
        // 是否允许发送cookie信息
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        // 允许的请求头信息
        configuration.addAllowedHeader("*");

        // 4.添加路径映射，我们拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);

    }
}
