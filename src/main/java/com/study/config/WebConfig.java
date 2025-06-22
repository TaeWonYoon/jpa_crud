package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //파일업로드 경로 변환셋팅
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/uploads/");
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() { // 폼 서브밋 사용시 RestApi Url 매핑 작용(get, post 외에 항목만)
        return new HiddenHttpMethodFilter();
    }
}