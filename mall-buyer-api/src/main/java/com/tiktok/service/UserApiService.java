package com.tiktok.service;

import com.tiktok.dto.UserLoginDTO;
import com.tiktok.dto.UserRegisterDTO;
import com.tiktok.entity.User;
import com.tiktok.vo.UserLoginVO;
import com.tiktok.vo.UserRegisterVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class UserApiService {

    @DubboReference
    private UserService userService;

    @DubboReference
    private AuthService authService;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        User user = userService.login(userLoginDTO);

        //登录成功后，生成jwt令牌
        String token = authService.deliverToken(user.getId());

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .token(token)
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
}
