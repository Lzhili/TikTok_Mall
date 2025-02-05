package com.tiktok.aspect;

import com.tiktok.annotation.AutoFill;
import com.tiktok.constant.AutoFillConstant;
import com.tiktok.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，统一为公共字段赋值
 */
@Slf4j
@Component
@Aspect
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.tiktok.mapper.*.*(..)) && @annotation(com.tiktok.annotation.AutoFill)")
    public void autoFillPointcut() {}

    /**
     * 前置通知
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始公共字段（时间）自动填充...");

        //获取到当前被拦截方法上的数据库操作类
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //获得方法上的注解对象
        OperationType operationType = autoFill.value(); //获得数据库操作类型

        //获取当前被拦截方法的方法参数-实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();

        //根据当前不同操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){
            //为两个个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                //通过反射来为属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            //为一个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                //通过反射来为属性赋值
                setUpdateTime.invoke(entity, now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
