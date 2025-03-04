package com.tiktok.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.config.RoleUpdateEvent;
import com.tiktok.dto.UserLoginDTO;
import com.tiktok.dto.UserRegisterDTO;
import com.tiktok.entity.User;
import com.tiktok.exception.AccountNotFoundException;
import com.tiktok.exception.IllegalRole;
import com.tiktok.vo.UserLoginVO;
import com.tiktok.vo.UserRegisterVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.text.MessageFormat;

@Service
public class UserApiService {

    @DubboReference
    private UserService userService;

    @DubboReference
    private AuthService authService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
//        String token = authService.deliverToken(user.getId());

        // 在 UserApiService 中，使用 Sa-Token 的 StpUtil 替代原有的 JWT 逻辑。
        System.out.println("----------- 登录前 ");
        System.out.println("Token值：" + StpUtil.getTokenValue());
        System.out.println("是否登录：" + StpUtil.isLogin());
        authService.login(user.getId(), user.getRole());
        System.out.println("----------- 登录后 ");
        System.out.println("Token值：" + StpUtil.getTokenValue());
        System.out.println("是否登录：" + StpUtil.isLogin());
//        StpUtil.login(user.getId()); // 使用 Sa-Token 登录

        String token = StpUtil.getTokenValue(); // 获取 Token，Token name 已经在 application.yml 文件中设置（写死了），故无需传递给前端
        Long expireTime = StpUtil.getTokenTimeout();

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
                .expireTime(expireTime)
                .build();

        return userLoginVO;
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {

        //注册成功返回id
        Long registerId = userService.register(userRegisterDTO);
        UserRegisterVO userRegisterVO = UserRegisterVO.builder()
                .id(registerId)
                .build();
        return userRegisterVO;
    }

    /**
     * 用户登出
     */
    public void logout() {
//        StpUtil.logout(); // 使用 Sa-Token 注销登录
        System.out.println("----------- 注销登录前 ");
        System.out.println("Token值：" + StpUtil.getTokenValue());
        System.out.println("是否登录：" + StpUtil.isLogin());
        authService.logout();
        System.out.println("----------- 注销登录后 ");
        System.out.println("Token值：" + StpUtil.getTokenValue());
        System.out.println("是否登录：" + StpUtil.isLogin());
    }

    @Transactional
    public void updateUserRole(Long userId, String newRole) throws AccountNotFoundException {
        // 只能修改角色为 user 或 admin
        if (!"user".equals(newRole) && !"admin".equals(newRole)) {
            throw new IllegalRole(MessageFormat.format("newRole: {0} 非法", newRole));
        }

        // 判断用户是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw new AccountNotFoundException("用户不存在");
        }

        // 更新用户角色
        userService.updateRoleById(userId, newRole);

        // 4. 更新Redis缓存（需在事务提交后执行）
        // 获取用户的 SaSession 并更新角色
//        SaSession accountSession = StpUtil.getSessionByLoginId(userId, false);
//        if (accountSession != null) {
//            accountSession.set("role", newRole);
//            // 手动触发会话更新（确保 Redis 同步）
//            accountSession.update();
//        }
        // 发布事件（触发Redis更新），监听器为 com.tiktok.listener.RoleUpdateEventListener
        applicationEventPublisher.publishEvent(new RoleUpdateEvent(this, userId, newRole));
    }
}
