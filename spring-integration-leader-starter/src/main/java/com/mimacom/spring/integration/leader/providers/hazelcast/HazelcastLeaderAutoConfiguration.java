package com.mimacom.spring.integration.leader.providers.hazelcast;

import java.util.UUID;

import com.hazelcast.core.HazelcastInstance;
import com.mimacom.spring.integration.leader.providers.LeaderConfigurationProperties;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.DefaultCandidate;

@Configuration
@ConditionalOnBean(HazelcastInstance.class)
@AutoConfigureAfter(HazelcastAutoConfiguration.class)
@ConditionalOnProperty(value = "spring-integration.leader.hazelcast.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LeaderConfigurationProperties.class)
@ConditionalOnMissingBean(LeaderProvider.class)
public class HazelcastLeaderAutoConfiguration {

    private final LeaderConfigurationProperties leaderConfigurationProperties;

    private final HazelcastInstance hazelcastInstance;

    public HazelcastLeaderAutoConfiguration(LeaderConfigurationProperties leaderConfigurationProperties, HazelcastInstance hazelcastInstance) {
        this.leaderConfigurationProperties = leaderConfigurationProperties;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Bean
    @ConditionalOnMissingBean(LeaderInitiator.class)
    public LeaderInitiator leaderInitiator() {
        DefaultCandidate candidate = new DefaultCandidate(
                UUID.randomUUID().toString(),
                leaderConfigurationProperties.getRole()
        );
        return new LeaderInitiator(this.hazelcastInstance, candidate);
    }

    @Bean
    public LeaderProvider leaderProvider(LeaderInitiator leaderInitiator) {
        return leaderInitiator::getContext;
    }
}
