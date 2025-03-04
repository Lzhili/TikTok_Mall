package com.tiktok.config;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.service.UserService;
import com.tiktok.entity.User;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SatokenPermissionConfiguration implements StpInterface {
    @DubboReference
    private UserService userService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 根据用户角色动态返回权限
//        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
//        User user = userService.getById(userId);
        List<String> roles = StpUtil.getRoleList();
        String role = roles.get(0);
        List<String> permissions = new ArrayList<>();

        // 管理员拥有所有权限
        if ("super-admin".equals(role)) {
            permissions.add("*"); // 通配符匹配所有接口
        } else if ("admin".equals(role)) {
            addCommonPermissions(permissions);

            // admin 能够增加或删除商品
            permissions.add("buyer:product:addOneProduct");
            permissions.add("buyer:product:deleteBatch");
            permissions.add("common:upload");
        } else if ("user".equals(role)){
            addCommonPermissions(permissions);
        }
        return permissions;
    }

    private void addCommonPermissions(List<String> permissions) {
        permissions.add("buyer:addressBook:*");
        permissions.add("buyer:shoppingCart:*");
        permissions.add("buyer:category:*");
        permissions.add("buyer:order:*");
        permissions.add("buyer:payment:*");
        permissions.add("buyer:product:page");
        permissions.add("buyer:product:getId"); // 匹配 get /buyer/product/{id}
        permissions.add("buyer:chatAi");
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 直接返回用户角色
//        return Collections.singletonList(userService.getUserRoleById(Long.valueOf(StpUtil.getLoginId().toString())));
        // 优先从缓存（Account-Session）中获取角色，若不存在则查询数据库并回填缓存
        // 获取当前用户的 Account-Session
        SaSession accountSession = StpUtil.getSessionByLoginId(loginId, false);

        // 从缓存中读取角色
        if (accountSession != null) {
            String role = accountSession.getString("role");
            if (role != null) {
                return Collections.singletonList(role);
            }
        }

        // 缓存未命中时，查询数据库并写入缓存
        String role = userService.getUserRoleById(Long.valueOf(loginId.toString()));

        if (accountSession != null) {
            accountSession.set("role", role);  // 仅补充缺失的 role
        } else {
            // 会话不存在时，创建新会话并设置 role
            SaSession newSession = StpUtil.getSessionByLoginId(loginId, true);
            newSession.set("role", role);
        }

        return Collections.singletonList(role);
    }
}
