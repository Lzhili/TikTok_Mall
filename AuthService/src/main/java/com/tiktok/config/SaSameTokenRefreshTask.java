package com.tiktok.config;

import cn.dev33.satoken.same.SaSameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Same-Token，定时刷新
 */
@Slf4j
@Configuration
public class SaSameTokenRefreshTask {
    // 从 0 分钟开始 每隔 5 分钟执行一次 Same-Token
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void refreshToken(){
        log.info("AuthService: Same-Token refresh");
        SaSameUtil.refreshToken();
    }
}

