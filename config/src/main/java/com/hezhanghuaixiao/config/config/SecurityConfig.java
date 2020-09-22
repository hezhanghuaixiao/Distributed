package com.hezhanghuaixiao.config.config;


import com.hezhanghuaixiao.config.component.DynamicAccessDecisionManager;
import com.hezhanghuaixiao.config.component.DynamicSecurityFilter;
import com.hezhanghuaixiao.config.component.RestfulAccessDeniedHandler;
import com.hezhanghuaixiao.config.component.DynamicSecurityMetadataSource;
import com.hezhanghuaixiao.config.component.DynamicSecurityService;
import com.hezhanghuaixiao.config.component.JwtAuthenticationTokenFilter;
import com.hezhanghuaixiao.config.component.RestAuthenticationEntryPoint;
import com.hezhanghuaixiao.config.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/*
 @describe 对SpringSecurity的配置的扩展，支持自定义白名单资源路径和查询用户逻辑
 @params
 @return
 @author 何章怀晓
 @date 2020/8/24  10:20
 @other
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  //当没有动态权限业务类时就不会创建动态权限相关对象，实现了有动态权限控制和没有这两种情况的兼容。
  //其他端如果鉴权，新建config继承此类，但是并不是所有的项目都需要鉴权，所以这里的对象并不是一定要注入
  @Autowired(required = false)
  private DynamicSecurityService dynamicSecurityService;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
        .authorizeRequests();
    //不需要保护的资源路径允许访问
    //对SpringSecurity的配置的扩展，支持自定义白名单资源路径和查询用户逻辑
    for (String url : ignoreUrlsConfig().getUrls()) {
      registry.antMatchers(url).permitAll();
    }
    //允许跨域请求的OPTIONS请求
    //OPTIONS请求即预检请求，可用于检测服务器允许的http方法。当发起跨域请求时，由于安全原因，触发一定条件时浏览器会在正式请求之前自动先发起OPTIONS请求，即CORS预检请求，服务器若接受该跨域请求，浏览器才继续发起正式请求
    registry.antMatchers(HttpMethod.OPTIONS)
        .permitAll();
    // 除了上面的所有都需要鉴权
    registry.and()
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        // 关闭跨站请求防护及不使用session
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // 自定义权限拒绝处理类
        .and()
        .exceptionHandling()
        .accessDeniedHandler(restfulAccessDeniedHandler())
        .authenticationEntryPoint(restAuthenticationEntryPoint())
        // 自定义权限拦截器JWT过滤器
        .and()
        .addFilterBefore(jwtAuthenticationTokenFilter(),
            UsernamePasswordAuthenticationFilter.class);
    //有动态权限配置时添加动态权限校验过滤器
    if (dynamicSecurityService != null) {
      //如果权限非空，说明该用户有自己对应的资源匹配权限
      //添加前置过滤器
      registry.and().addFilterBefore(dynamicSecurityFilter(), FilterSecurityInterceptor.class);
    }
  }

  //配置userDetailsService和密码编辑器
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //我在MallSecurityConfig类中定义的实现类==》SpringSecurity定义的核心接口，用于根据用户名获取用户信息，需要自行实现
    //我在AdminUserDetails勒种的实现类 UserDetails(主要是用户信息和权限集合)
    auth.userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录
  @Bean
  public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
    return new JwtAuthenticationTokenFilter();
  }


  //OAuth2.0的密码模式  认证管理器
  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  //当用户没有访问权限时的处理器，用于返回JSON格式的处理结果
  @Bean
  public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
    return new RestfulAccessDeniedHandler();
  }

  //当未登录或token失效时，返回JSON格式的结果；
  @Bean
  public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
    return new RestAuthenticationEntryPoint();
  }

  //白名单路径集合
  @Bean
  public IgnoreUrlsConfig ignoreUrlsConfig() {
    return new IgnoreUrlsConfig();
  }

  //JWT
  @Bean
  public JwtTokenUtil jwtTokenUtil() {
    return new JwtTokenUtil();
  }

  //动态权限过滤器，用于实现基于路径的动态权限过滤
  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
    return new DynamicAccessDecisionManager();
  }


  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicSecurityFilter dynamicSecurityFilter() {
    return new DynamicSecurityFilter();
  }

  //动态权限数据源，用于获取动态权限规则
  @ConditionalOnBean(name = "dynamicSecurityService")
  @Bean
  public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
    return new DynamicSecurityMetadataSource();
  }

}
