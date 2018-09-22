package com.mimacom.spring.integration.leader.providers.lockregistry;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;

@Configuration
@ConditionalOnBean(LockRegistryLeaderInitiator.class)
public class LockRegistryLeaderAutoConfiguration {

    private final LockRegistryLeaderInitiator lockRegistryLeaderInitiator;

    public LockRegistryLeaderAutoConfiguration(LockRegistryLeaderInitiator lockRegistryLeaderInitiator) {
        this.lockRegistryLeaderInitiator = lockRegistryLeaderInitiator;
    }

    @Bean
    public LeaderProvider leaderProvider() {
        return lockRegistryLeaderInitiator::getContext;
    }
}
