package com.tiktok.service;

public interface AuthService {
    String deliverToken(Long user_id);

    void login(Long id, String role);

    void logout();
}
