package com.tiktok.exception;

/**
 * 用户地址簿为空的异常
 */
public class AddressBookIsNullException extends BaseException {
    public AddressBookIsNullException() {
    }

    public AddressBookIsNullException(String msg) {
        super(msg);
    }
}
