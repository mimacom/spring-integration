package com.mimacom.spring.integration.leader.providers.zookeeper;

import com.mimacom.spring.integration.leader.LeaderConfigurationProperties;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import org.apache.curator.framework.CuratorFramework;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;

@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnBean(CuratorFramework.class)
@EnableConfigurationProperties({ZookeeperLeaderConfigurationProperties.class, LeaderConfigurationProperties.class})
@ConditionalOnProperty(value = "spring-integration.leader.zookeeper.enabled", matchIfMissing = true)
public class ZookeeperLeaderAutoConfiguration {

    private final ZookeeperLeaderConfigurationProperties zookeeperLeaderConfigurationProperties;

    private final LeaderConfigurationProperties leaderConfigurationProperties;

    private final CuratorFramework curatorFramework;

    public ZookeeperLeaderAutoConfiguration(ZookeeperLeaderConfigurationProperties zookeeperLeaderConfigurationProperties, LeaderConfigurationProperties leaderConfigurationProperties, CuratorFramework curatorFramework) {
        this.zookeeperLeaderConfigurationProperties = zookeeperLeaderConfigurationProperties;
        this.leaderConfigurationProperties = leaderConfigurationProperties;
        this.curatorFramework = curatorFramework;
    }

    @Bean
    @ConditionalOnMissingBean(LeaderInitiator.class)
    LeaderInitiatorFactoryBean leaderInitiator() {
        return new LeaderInitiatorFactoryBean()
                .setClient(curatorFramework)
                .setPath(zookeeperLeaderConfigurationProperties.getPath())
                .setRole(leaderConfigurationProperties.getRole());
    }

    @Bean
    public LeaderProvider leaderProvider(LeaderInitiator leaderInitiator) {
        return leaderInitiator::getContext;
    }

}
