package com.tiktok.service;

import com.tiktok.dto.UserLoginDTO;
import com.tiktok.dto.UserRegisterDTO;
import com.tiktok.entity.User;

public interface UserService {

    User login(UserLoginDTO userLoginDTO);

    Long register(UserRegisterDTO userRegisterDTO);

    User getById(Long id);

    void updateRoleById(Long id, String role);

    String getUserRoleById(Long id);
}
