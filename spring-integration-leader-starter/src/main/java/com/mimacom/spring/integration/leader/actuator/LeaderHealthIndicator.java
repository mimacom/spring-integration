package com.mimacom.spring.integration.leader.actuator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.integration.leader.Context;

public class LeaderHealthIndicator extends AbstractHealthIndicator {

    private final List<LeaderProvider> leaderProvider = new ArrayList<>();

    LeaderHealthIndicator(List<LeaderProvider> leaderProvider) {
        this.leaderProvider.addAll(leaderProvider);
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        for (LeaderProvider provider : leaderProvider) {
            Context context = Objects.requireNonNull(provider.context());
            builder.up().withDetail("is-leader", context.isLeader());
            if (context.getRole() != null) {
                builder.withDetail("role", context.getRole());
            }
        }

    }
}
