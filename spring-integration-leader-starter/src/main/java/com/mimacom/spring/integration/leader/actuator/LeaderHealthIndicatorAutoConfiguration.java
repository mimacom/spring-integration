package com.mimacom.spring.integration.leader.actuator;


import java.util.List;

import com.mimacom.spring.integration.leader.LeaderAwareAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.OnConditionalLeaderProvider;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(HealthIndicator.class)
@ConditionalOnEnabledHealthIndicator("leader")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter(LeaderAwareAutoConfiguration.class)
@OnConditionalLeaderProvider
public class LeaderHealthIndicatorAutoConfiguration {

    private List<LeaderProvider> leaderProviders;

    public LeaderHealthIndicatorAutoConfiguration(List<LeaderProvider> leaderProviders) {
        this.leaderProviders = leaderProviders;
    }

    @Bean
    LeaderHealthIndicator leaderHealthIndicator() {
        return new LeaderHealthIndicator(this.leaderProviders);
    }

}
