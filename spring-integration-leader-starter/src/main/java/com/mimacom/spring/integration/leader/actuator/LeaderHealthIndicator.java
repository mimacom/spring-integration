package com.mimacom.spring.integration.leader.actuator;

import java.util.Objects;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.integration.leader.Context;

public class LeaderHealthIndicator extends AbstractHealthIndicator {

    private final LeaderProvider leaderProvider;

    LeaderHealthIndicator(LeaderProvider leaderProvider) {
        this.leaderProvider = leaderProvider;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        Context context = Objects.requireNonNull(leaderProvider.context());
        builder.up().withDetail("is-leader", context.isLeader());
        if (context.getRole() != null) {
            builder.withDetail("role", context.getRole());
        }
    }
}
