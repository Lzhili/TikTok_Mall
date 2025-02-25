package com.tiktok.aspect;

import com.alibaba.fastjson.JSONObject;
import com.tiktok.context.BaseContext;
import com.tiktok.entity.OperateLog;
import com.tiktok.mapper.OperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.tiktok.annotation.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //操作人ID-当前登录user的ID
        Long operateUser = BaseContext.getCurrentId();

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类名
        String className = joinPoint.getTarget().getClass().getName();

        //操作方法名
        String methodName = joinPoint.getSignature().getName();

        //操作方法参数
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis(); //开始时间
        // 调用原始目标方法
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis(); //结束时间

        //方法返回值
        String returnValue = JSONObject.toJSONString(result);

        //操作耗时
        Long costTime = end - begin;

        //记录日志操作
        OperateLog operateLog =
                new OperateLog(null, operateUser,operateTime,className,
                        methodName,methodParams,returnValue,costTime);
        operateLogMapper.insert(operateLog);

        log.info("AOP记录日志：{}", operateLog);

        return result;
    }
}
