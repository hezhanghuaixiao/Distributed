package com.hezhanghuaixiao.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 @describe MyBatis配置类
 @author 何章怀晓 hezhanghuaixiao
 @date 2020/9/22  17:41
 @other
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.hezhanghuaixiao.admin.dao"})
public class MyBatisConfig {
}
