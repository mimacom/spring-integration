package com.mimacom.spring.integration.zookeeper;

import org.apache.curator.framework.CuratorFramework;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.zookeeper.config.CuratorFrameworkFactoryBean;

@Configuration
@ConditionalOnMissingBean(CuratorFramework.class)
@EnableConfigurationProperties(ZookeeperConfigurationProperties.class)
public class ZookeeperAutoConfiguration {
    
    private final ZookeeperConfigurationProperties zookeeperConfigurationProperties;

    public ZookeeperAutoConfiguration(ZookeeperConfigurationProperties zookeeperConfigurationProperties) {
        this.zookeeperConfigurationProperties = zookeeperConfigurationProperties;
    }

    @Bean
    CuratorFrameworkFactoryBean curatorFramework() {
        return new CuratorFrameworkFactoryBean(this.zookeeperConfigurationProperties.getConnection());
    }

}
