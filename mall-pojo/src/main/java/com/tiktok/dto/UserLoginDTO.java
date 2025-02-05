package com.tiktok.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户登录时传递的数据模型")
public class UserLoginDTO implements Serializable {

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "密码")
    private String password;
}

