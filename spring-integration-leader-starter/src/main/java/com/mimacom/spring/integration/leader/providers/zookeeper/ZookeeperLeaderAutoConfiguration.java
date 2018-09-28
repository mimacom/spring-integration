package com.mimacom.spring.integration.leader.providers.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ZookeeperLeaderInitiatorRegistrar.class, ZookeeperLeaderProviderRegistrar.class})
@AutoConfigureAfter(name = {"org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration"})
@ConditionalOnBean(CuratorFramework.class)
@ConditionalOnProperty(value = "spring-integration.leader.zookeeper.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ZookeeperLeaderConfigurationProperties.class)
public class ZookeeperLeaderAutoConfiguration {


}
