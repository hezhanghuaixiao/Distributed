package com.hezhanghuaixiao.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName WebMvcConfig
 * @Auther 何章怀晓
 * @Date 2020/8/24  15:34
 * @vERSION 1.0
 * @Use 一句话说明本类功能
 **/
@Configuration
public class WebMvcConfig  extends WebMvcConfigurerAdapter {
 @Override
 public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("swagger-ui.html")
     .addResourceLocations("classpath:/META-INF/resources/");

     registry.addResourceHandler("/webjars/**")
     .addResourceLocations("classpath:/META-INF/resources/webjars/");
     }
 }
