package com.tiktok.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tiktok.jwt")
@Data
public class JwtProperties {

    /**
     * 用户端用户生成jwt令牌相关配置
     */
    private String userSecretKey;  //jwt签名加密时使用的秘钥
    private long userTtl;  //jwt过期时间
    private String userTokenName; // 前端传递过来的令牌名称

}
