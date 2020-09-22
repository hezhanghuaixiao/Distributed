package com.hezhanghuaixiao.config.component;

import cn.hutool.core.collection.CollUtil;
import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/*
 @describe 动态权限决策管理器，用于判断用户是否有访问权限
            1.所有请求super.beforeInvocation(fi)进行鉴权此方法调用
            2.AccessDecisionManager中的decide方法进行鉴权操作
            3.decide方法中的configAttributes参数会通过SecurityMetadataSource中的getAttributes方法来获取，
            configAttributes其实就是配置好的访问当前接口所需要的权限
 @params
 @return
 @author 何章怀晓
 @date 2020/8/17  11:43
 @other
 */
public class DynamicAccessDecisionManager implements AccessDecisionManager {
    /*
     @describe AccessDecisionManager接口来实现权限校验，对于没有配置资源的接口我们直接允许访问，
               对于配置了资源的接口，我们把访问所需资源和用户拥有的资源进行比对，如果匹配则允许访问。
     @params
     @return
     @author 何章怀晓
     @date 2020/8/17  11:46
     @other
     */
    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        // 当接口未被配置资源时直接放行
        if (CollUtil.isEmpty(configAttributes)) {
            return;
        }
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            //将访问所需资源或用户拥有资源进行比对
            String needAuthority = configAttribute.getAttribute();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
