package com.hezhanghuaixiao.config.component;

import java.util.Map;
import org.springframework.security.access.ConfigAttribute;

/*
 @describe 动态权限相关业务类
 @params
 @return
 @author 何章怀晓
 @date 2020/8/17  11:55
 @other
 */
public interface DynamicSecurityService {

  /*
   @describe 加载资源ANT通配符和资源对应MAP
              ANT ==》匹配资源路径  MAP==》资源路径
   @params
   @return
   @author 何章怀晓
   @date 2020/8/14  19:02
   @other
   */
  Map<String, ConfigAttribute> loadDataSource();
}
