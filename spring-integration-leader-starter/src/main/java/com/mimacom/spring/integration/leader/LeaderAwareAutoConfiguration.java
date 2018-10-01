package com.mimacom.spring.integration.leader;

import java.util.List;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.OnConditionalLeaderProvider;
import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.lockregistry.LockRegistryLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.zookeeper.ZookeeperLeaderAutoConfiguration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@AutoConfigureAfter({
        ZookeeperLeaderAutoConfiguration.class,
        HazelcastLeaderAutoConfiguration.class,
        LockRegistryLeaderAutoConfiguration.class
})
@EnableConfigurationProperties(LeaderAwareConfigurationProperties.class)
public class LeaderAwareAutoConfiguration {

    private final LeaderAwareConfigurationProperties leaderAwareConfigurationProperties;

    LeaderAwareAutoConfiguration(LeaderAwareConfigurationProperties leaderAwareConfigurationProperties) {
        this.leaderAwareConfigurationProperties = leaderAwareConfigurationProperties;
    }

    @Bean
    @OnConditionalLeaderProvider
    LeaderAwareEndpointRolePostProcessor leaderAwareEndpointPostProcessor() {
        return new LeaderAwareEndpointRolePostProcessor(leaderAwareConfigurationProperties.getRoles());
    }

    @Bean
    @OnConditionalLeaderProvider
    public LeaderAwareAspect leaderAwareAspect() {
        return new LeaderAwareAspect();
    }

    @Bean
    public LeaderProviderValidator leaderProviderValidator(ObjectProvider<List<LeaderProvider>> leaderProviders) {
        return new LeaderProviderValidator(leaderProviders);
    }

    static class LeaderProviderValidator implements InitializingBean {

        private final ObjectProvider<List<LeaderProvider>> leaderProviders;

        LeaderProviderValidator(ObjectProvider<List<LeaderProvider>> leaderProviders) {
            this.leaderProviders = leaderProviders;
        }

        @Override
        public void afterPropertiesSet() {
            Assert.notNull(this.leaderProviders.getIfAvailable(),
                    () -> "No leader provider could be auto-configured, check your configuration");
        }

    }

}
