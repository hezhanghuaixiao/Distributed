package com.hezhanghuaixiao.config.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 @describe 用于配置不需要保护的资源路径
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  10:20
 @other
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();

}
