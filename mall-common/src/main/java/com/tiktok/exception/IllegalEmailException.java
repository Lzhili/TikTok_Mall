package com.tiktok.exception;

public class IllegalEmailException extends BaseException {
    public IllegalEmailException() {
    }

    public IllegalEmailException(String msg) {
        super(msg);
    }
}
