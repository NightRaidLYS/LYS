package com.lys.aspect;

import com.alibaba.fastjson.JSON;
import com.lys.annotation.SystemLog;
import com.lys.sys.entity.SysLog;
import com.lys.sys.filter.Address;
import com.lys.sys.filter.ClientHostAddress;
import com.lys.sys.service.ISysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 *  日志AOP增强
 *     boge3306[V]
 */
@Component
@Aspect
@EnableAspectJAutoProxy
public class SysLogAspect {


    @Autowired
    ISysLogService sysLogService;


    @Pointcut("@annotation(com.lys.annotation.SystemLog)")
    public void logPointcut(){

    }


    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        // 执行的开始时间
        long begin = System.currentTimeMillis();
        Object obj = point.proceed();
        // 获取到 目标方法执行的时长
        long time = System.currentTimeMillis() - begin;
        // 保存日志操作的信息
        saveSysLog(point,time);

        return obj;
    }

    /**
     * 保存日志相关的方法
     *     操作的用户
     *     具体的操作
     *     操作的方法名称
     *     参数列表
     *     执行的时长
     *     客户端的ip
     *     记录创建的时间
     * @param point
     */
    private void saveSysLog(ProceedingJoinPoint point,long time) {
        ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<>();
        SysLog sysLog = new SysLog();
        // TODO 当前登录的用户 和 IP信息
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        // 从线程本地存储中获取HttpServletRequest对象
//        HttpServletRequest request = threadLocalRequest.get();
//        String ip= ClientHostAddress.getClientHostAddress(request);
        //System.out.println(ip);
        Object username=authentication.getPrincipal();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 需要获取 方法@SysLog 注解的 描述信息
        SystemLog systemLogAnno = method.getAnnotation(SystemLog.class);
        if(systemLogAnno != null){
            // 绑定 operation 的值
            sysLog.setOperation(systemLogAnno.value());
        }
        // 绑定方法的名称
        String className = point.getTarget().getClass().getName();
        String methodName = method.getName();
        sysLog.setMethod(className+"."+methodName+"()");

        // 获取方法的参数
        Object[] paramsArgs = point.getArgs();
        String params = JSON.toJSON(paramsArgs).toString();
        sysLog.setParams(params);

        // 方法调用的执行时长
        sysLog.setTime(time);
        // 然后记录的创建时间
        sysLog.setCreateDate(LocalDateTime.now());
        sysLog.setUsername((String) username);
        //sysLog.setIp(ip);
        sysLogService.save(sysLog);

    }
}
