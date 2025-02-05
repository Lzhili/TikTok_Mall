package com.tiktok.handler;

import com.tiktok.constant.MessageConstant;
import com.tiktok.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * dubbo的ExceptionFilter会对dubbo调用中发生的异常进行一系列处理，最后再包装成RuntimeException抛给服务调用方
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(RuntimeException ex){
        String error_message = ex.getMessage();

        // 代替SQLIntegrityConstraintViolationException异常的处理器
        if (error_message.contains("Duplicate entry")){
            String[] split = error_message.split(" ");
            String msg = split[10];
            String message = msg + MessageConstant.ALREADY_EXISTS;
            log.error("异常信息：{}", message);
            return Result.error(message);
        }

        //一般的BaseException处理

        // 查找第一个 ":" 和 "\r" 的索引
        int startIndex = error_message.indexOf(":") + 2;  // 加2是为了跳过": "
        int endIndex = error_message.indexOf("\r");

        // 提取子字符串
        if (startIndex >= 0 && endIndex > startIndex) {
            error_message = error_message.substring(startIndex, endIndex);
        } else {
            log.info("未找到有效的错误信息");
        }
        log.error("异常信息：{}", error_message);
        return Result.error(error_message);
    }

}
