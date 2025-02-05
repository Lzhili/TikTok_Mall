package com.tiktok.exception;

/**
 * 注册密码二次验证不一致
 */
public class PasswordConfirmErrorException extends BaseException {
    public PasswordConfirmErrorException() {
    }

    public PasswordConfirmErrorException(String msg) {
        super(msg);
    }
}
