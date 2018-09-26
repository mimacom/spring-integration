package com.mimacom.spring.integration.leader.providers.zookeeper;

import com.mimacom.spring.integration.leader.LeaderAwareConfigurationProperties;
import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorPostProcessor;
import com.mimacom.spring.integration.leader.providers.ConditionalOnLeaderAwareRoles;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderMarker;
import com.mimacom.spring.integration.leader.providers.LeaderProviderPostProcessor;
import org.apache.curator.framework.CuratorFramework;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.leader.Context;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;

@Configuration
@AutoConfigureAfter(name = {
        "org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration",
        "com.mimacom.spring.integration.zookeeper.ZookeeperAutoConfiguration"
})
@ConditionalOnBean(CuratorFramework.class)
@ConditionalOnProperty(value = "spring-integration.leader.zookeeper.enabled", matchIfMissing = true)
@EnableConfigurationProperties({LeaderAwareConfigurationProperties.class, ZookeeperLeaderConfigurationProperties.class})
public class ZookeeperLeaderAutoConfiguration {

    @Bean
    public LeaderProviderMarker providerMarker() {
        return new LeaderProviderMarker();
    }

    @Bean
    @ConditionalOnLeaderAwareRoles
    public static ZookeeperLeaderInitiatorPostProcessor zookeeperLeaderInitiatorPostProcessor() {
        return new ZookeeperLeaderInitiatorPostProcessor();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor zookeeperLeaderProviderPostProcessor() {
        return new LeaderProviderPostProcessor<>(LeaderInitiator.class, ZookeeperLeaderProvider.class);
    }

    private static class ZookeeperLeaderInitiatorPostProcessor extends AbstractLeaderInitiatorPostProcessor {

        @Override
        protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher) {
            CuratorFramework curatorFramework = beanFactory.getBean(CuratorFramework.class);
            ZookeeperLeaderConfigurationProperties zookeeperLeaderConfigurationProperties = beanFactory.getBean(ZookeeperLeaderConfigurationProperties.class);
            return BeanDefinitionBuilder.genericBeanDefinition(LeaderInitiatorFactoryBeanAdapter.class)
                    .addConstructorArgValue(curatorFramework)
                    .addConstructorArgValue(role)
                    .addConstructorArgValue(zookeeperLeaderConfigurationProperties.getPath())
                    .getBeanDefinition();
        }

        static class LeaderInitiatorFactoryBeanAdapter extends LeaderInitiatorFactoryBean {

            public LeaderInitiatorFactoryBeanAdapter(CuratorFramework client, String role, String path) {
                super();
                super.setClient(client)
                        .setRole(role)
                        .setPath(path);
            }

        }
    }

    static class ZookeeperLeaderProvider implements LeaderProvider {

        private final LeaderInitiator leaderInitiator;

        ZookeeperLeaderProvider(LeaderInitiator leaderInitiator) {
            this.leaderInitiator = leaderInitiator;
        }

        @Override
        public Context context() {
            return leaderInitiator.getContext();
        }
    }
}
