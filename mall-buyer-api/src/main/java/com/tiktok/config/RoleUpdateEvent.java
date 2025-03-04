package com.tiktok.config;

import org.springframework.context.ApplicationEvent;


public class RoleUpdateEvent extends ApplicationEvent {
    private final Long userId;
    private final String newRole;

    public RoleUpdateEvent(Object source, Long userId, String newRole) {
        super(source);
        this.userId = userId;
        this.newRole = newRole;
    }

    // Getter方法
    public Long getUserId() { return userId; }
    public String getNewRole() { return newRole; }
}