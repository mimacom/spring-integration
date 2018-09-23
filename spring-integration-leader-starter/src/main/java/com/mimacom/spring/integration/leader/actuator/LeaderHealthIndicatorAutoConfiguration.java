package com.mimacom.spring.integration.leader.actuator;


import com.mimacom.spring.integration.leader.LeaderAwareAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(LeaderProvider.class)
@ConditionalOnClass(HealthIndicator.class)
@ConditionalOnEnabledHealthIndicator("leader")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(LeaderAwareAutoConfiguration.class)
public class LeaderHealthIndicatorAutoConfiguration {

    private final LeaderProvider leaderProvider;

    public LeaderHealthIndicatorAutoConfiguration(LeaderProvider leaderProvider) {
        this.leaderProvider = leaderProvider;
    }

    @Bean
    LeaderHealthIndicator leaderHealthIndicator() {
        return new LeaderHealthIndicator(this.leaderProvider);
    }

}
