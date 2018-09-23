package com.mimacom.spring.integration.leader;

import java.util.Objects;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.lockregistry.LockRegistryLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.zookeeper.ZookeeperLeaderAutoConfiguration;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.leader.Context;

@Configuration
@AutoConfigureAfter({
        ZookeeperLeaderAutoConfiguration.class,
        HazelcastLeaderAutoConfiguration.class,
        LockRegistryLeaderAutoConfiguration.class
})
@ConditionalOnBean(LeaderProvider.class)
@EnableConfigurationProperties(LeaderAwareConfigurationProperties.class)
class LeaderAwareAutoConfiguration {

    private final LeaderAwareConfigurationProperties leaderAwareConfigurationProperties;

    LeaderAwareAutoConfiguration(LeaderAwareConfigurationProperties leaderAwareConfigurationProperties) {
        this.leaderAwareConfigurationProperties = leaderAwareConfigurationProperties;
    }

    @Bean
    LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor() {
        return new LeaderAwareEndpointPostProcessor(this.leaderAwareConfigurationProperties.getEndpoints());
    }

    @Configuration
    @ConditionalOnBean(LeaderProvider.class)
    @ConditionalOnClass(HealthIndicator.class)
    @AutoConfigureAfter({HealthIndicatorAutoConfiguration.class})
    static class LeaderHealthAutoConfiguration {

        @Bean
        @ConditionalOnEnabledHealthIndicator("leader")
        HealthIndicator leaderHealthIndicator(LeaderProvider leaderProvider) {
            return new AbstractHealthIndicator() {
                @Override
                protected void doHealthCheck(Health.Builder builder) {
                    Context context = Objects.requireNonNull(leaderProvider.context());
                    builder.up().withDetail("is-leader", context.isLeader());
                    if (context.getRole() != null) {
                        builder.withDetail("role", context.getRole());
                    }
                }
            };
        }
    }

}
