package com.tiktok.controller;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.annotation.Log;
import com.tiktok.dto.UserLoginDTO;
import com.tiktok.dto.UserRegisterDTO;
import com.tiktok.result.Result;
import com.tiktok.service.UserApiService;
import com.tiktok.vo.UserLoginVO;
import com.tiktok.vo.UserRegisterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "用户接口", description = "用户接口")
@RequestMapping("/buyer/user")
public class UserController {

    @Autowired
    private UserApiService userApiService;

    /**
     * 用户登录方法
     * @param userLoginDTO
     * @return
     */
    @Operation(summary = "用户登录方法")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("用户登录：{}", userLoginDTO);
        UserLoginVO userLoginVO = userApiService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * 用户注册方法
     * @param userRegisterDTO
     * @return
     */
    @Log
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserRegisterVO> register(@RequestBody UserRegisterDTO userRegisterDTO){
        log.info("用户注册：{}", userRegisterDTO);
        UserRegisterVO userRegisterVO = userApiService.register(userRegisterDTO);
        return Result.success(userRegisterVO);
    }

    /**
     * 用户退出登录
     *
     * @return
     */
    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        log.info("用户退出登录");
        userApiService.logout();
        return Result.success();
    }

    /**
     *
     * @param userId
     * @param newRole
     * @return
     */
    @Operation(summary = "更新用户角色")
    @PostMapping("/updateRole")
    public Result<String> updateUserRole(@RequestParam Long userId, @RequestParam String newRole) {
        // 1. 校验当前用户是否为管理员
//        if (!StpUtil.hasRole("super-admin")) {
//            throw new NotPermissionException("无权限操作");
//        }
        log.info("更改用户 {} 的角色为 {}", userId, newRole);

        userApiService.updateUserRole(userId, newRole);

        return Result.success("角色更新成功");
    }
}
