package com.tiktok.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "用户注册时传递的数据模型")
public class UserRegisterDTO implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "确认密码")
    private String confirm_password ;
}

