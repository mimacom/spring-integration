package com.mimacom.spring.integration.leader.providers;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.OnPropertyListCondition;

class OnLeaderAwareRolesCondition extends OnPropertyListCondition {

    protected OnLeaderAwareRolesCondition() {
        super("spring-integration.leader-aware.roles",
                () -> ConditionMessage.forCondition("Leader-Aware Roles"));
    }
}
