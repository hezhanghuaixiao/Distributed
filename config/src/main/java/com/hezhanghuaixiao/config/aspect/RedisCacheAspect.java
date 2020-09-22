package com.hezhanghuaixiao.config.aspect;

import com.hezhanghuaixiao.config.annotation.CacheException;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 @describe Redis缓存切面，防止Redis宕机影响正常业务逻辑
 @params
 @return
 @author 何章怀晓
 @date 2020/8/14  10:53
 @other
 */
/*@Aspect
@Component
//@Order标记定义了组件的加载顺序，值越小拥有越高的优先级，可为负数。
//例如：
//@Order(-1)优先于@Order(0)
//@Order(1)优先于@Order(2)
@Order(2)*/
public class RedisCacheAspect {
    private static Logger LOGGER = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Pointcut(value = "execution(public * com.hezhanghuaixiao.config.service.*CacheService.*(..))")
    public void cacheAspect() {
    }
    /*
     @describe JoinPoint接口 ==》获取代理类和被代理类的信息
     @params
     @return
     @author 何章怀晓
     @date 2020/8/14  10:36
     @other
     */
    //@Around()==》此注解进行切片捕捉，捕捉切点方法
    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object result = null;
        try {
            //实际执行切片对象方法。
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            //有CacheException注解的方法需要抛出异常
            if (method.isAnnotationPresent(CacheException.class)) {
                throw throwable;
            } else {
                LOGGER.error(throwable.getMessage());
            }
        }
        return result;
    }

}
