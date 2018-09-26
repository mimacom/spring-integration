package com.mimacom.spring.integration.leader;

import com.mimacom.spring.integration.leader.providers.OnConditionalLeaderProvider;
import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.lockregistry.LockRegistryLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.zookeeper.ZookeeperLeaderAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({
        ZookeeperLeaderAutoConfiguration.class,
        HazelcastLeaderAutoConfiguration.class,
        LockRegistryLeaderAutoConfiguration.class
})
@EnableConfigurationProperties(LeaderAwareConfigurationProperties.class)
@OnConditionalLeaderProvider
public class LeaderAwareAutoConfiguration {

    private final LeaderAwareConfigurationProperties leaderAwareConfigurationProperties;

    LeaderAwareAutoConfiguration(LeaderAwareConfigurationProperties leaderAwareConfigurationProperties) {
        this.leaderAwareConfigurationProperties = leaderAwareConfigurationProperties;
    }

    @Bean
    LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor() {
        return new LeaderAwareEndpointPostProcessor(
                this.leaderAwareConfigurationProperties.getDefaultRole(), this.leaderAwareConfigurationProperties.getEndpoints()
        );
    }

}
