package com.tiktok.listener;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.tiktok.config.RoleUpdateEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class RoleUpdateEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRoleUpdate(RoleUpdateEvent event) {
        Long userId = event.getUserId();
        String newRole = event.getNewRole();

        SaSession accountSession = StpUtil.getSessionByLoginId(userId, false);
        if (accountSession != null) {
            accountSession.set("role", newRole);
            accountSession.update(); // 强制同步到Redis
        }
    }
}