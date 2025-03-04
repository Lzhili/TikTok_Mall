package com.tiktok.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.constant.JwtClaimsConstant;
import com.tiktok.properties.JwtProperties;
import com.tiktok.service.AuthService;
import com.tiktok.service.UserService;
import com.tiktok.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
//@Transactional, 加上interfaceClass事务注解才能生效
@DubboService(interfaceClass = AuthService.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public String deliverToken(Long user_id) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user_id);
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        return token;
    }

    @Override
    public void login(Long id, String role) {
        StpUtil.login(id);
        // 将角色信息存入 Account-Session
        SaSession accountSession = StpUtil.getSessionByLoginId(id);
        accountSession.set("role", role);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }
}
