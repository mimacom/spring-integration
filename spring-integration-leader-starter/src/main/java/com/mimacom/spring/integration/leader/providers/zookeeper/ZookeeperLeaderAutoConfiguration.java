package com.mimacom.spring.integration.leader.providers.zookeeper;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderTypeCondition;
import org.apache.curator.framework.CuratorFramework;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(name = {"org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration"})
@Import({ZookeeperLeaderInitiatorRegistrar.class, ZookeeperLeaderProviderRegistrar.class})
@ConditionalOnSingleCandidate(CuratorFramework.class)
@ConditionalOnMissingBean(LeaderProvider.class)
@Conditional(LeaderProviderTypeCondition.class)
@EnableConfigurationProperties(ZookeeperLeaderConfigurationProperties.class)
public class ZookeeperLeaderAutoConfiguration {

}
