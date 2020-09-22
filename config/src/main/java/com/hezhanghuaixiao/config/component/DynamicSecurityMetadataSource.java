package com.hezhanghuaixiao.config.component;

import cn.hutool.core.util.URLUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/*
 @describe 动态权限数据源，用于获取动态权限规则
 @params
 @return
 @author 何章怀晓
 @date 2020/8/17  11:32
 @other
 */
public class DynamicSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static Map<String, ConfigAttribute> configAttributeMap = null;
    @Autowired
    private DynamicSecurityService dynamicSecurityService;


    /*
     @describe @PostConstruct服务器加载Servlet的时候运行，并且只会被服务器执行一次。
                PostConstruct在构造函数之后执行，init（）方法之前执行。
               【非静态的void（）方法】
               项目启动时获取所有资源url  加载所有的后台资源规则
     @params
     @return
     @author 何章怀晓
     @date 2020/8/17  11:27
     @other
     */
    @PostConstruct
    public void loadDataSource() {
        configAttributeMap = dynamicSecurityService.loadDataSource();
    }
    /*
     @describe 清空map集合里的数据
                由于我们的后台资源规则被缓存在了一个Map对象之中，
                所以当后台资源发生变化时，我们需要清空缓存的数据，然后下次查询时就会被重新加载进来
     @params
     @return 
     @author 何章怀晓
     @date 2020/8/17  11:39
     @other
     */
    public void clearDataSource() {
        //清空map集合里的信息
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    /*
     @describe 实现该方法==》用于获取当前访问路径所需权限（每个用户有角色，角色关联资源，获取所有资源和当前用户访问资源进行比对）
     @params
     @return
     @author 何章怀晓
     @date 2020/8/17  11:27
     @other
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        //获取所有配置的资源信息
        if (configAttributeMap == null) this.loadDataSource();
        List<ConfigAttribute>  configAttributes = new ArrayList<>();
        //获取当前访问的路径资源
        String url = ((FilterInvocation) o).getRequestUrl();
        String path = URLUtil.getPath(url);
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> iterator = configAttributeMap.keySet().iterator();
        //获取访问该路径所需资源
        while (iterator.hasNext()) {
            String pattern = iterator.next();
            //匹配当前请求路径和系统所有路径
            if (pathMatcher.match(pattern, path)) {
                //匹配将该资源放到configAttributes集合，说明该资源用户存在相同资源，标识可以访问
                configAttributes.add(configAttributeMap.get(pattern));
            }
        }
        // 未设置操作请求权限，返回空集合
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
