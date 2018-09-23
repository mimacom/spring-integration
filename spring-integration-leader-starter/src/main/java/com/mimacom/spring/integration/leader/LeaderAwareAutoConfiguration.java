package com.mimacom.spring.integration.leader;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.lockregistry.LockRegistryLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.zookeeper.ZookeeperLeaderAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({
        ZookeeperLeaderAutoConfiguration.class,
        HazelcastLeaderAutoConfiguration.class,
        LockRegistryLeaderAutoConfiguration.class
})
@ConditionalOnBean(LeaderProvider.class)
@EnableConfigurationProperties(LeaderAwareConfigurationProperties.class)
public class LeaderAwareAutoConfiguration {

    private final LeaderAwareConfigurationProperties leaderAwareConfigurationProperties;

    LeaderAwareAutoConfiguration(LeaderAwareConfigurationProperties leaderAwareConfigurationProperties) {
        this.leaderAwareConfigurationProperties = leaderAwareConfigurationProperties;
    }

    @Bean
    LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor() {
        return new LeaderAwareEndpointPostProcessor(this.leaderAwareConfigurationProperties.getEndpoints());
    }

}
