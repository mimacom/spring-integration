package com.mimacom.spring.integration.leader.providers.hazelcast;

import java.util.UUID;

import com.hazelcast.core.HazelcastInstance;
import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorPostProcessor;
import com.mimacom.spring.integration.leader.providers.LeaderConfigurationProperties;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderMarker;
import com.mimacom.spring.integration.leader.providers.LeaderProviderPostProcessor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;

@Configuration
@ConditionalOnBean(HazelcastInstance.class)
@AutoConfigureAfter(HazelcastAutoConfiguration.class)
@ConditionalOnProperty(value = "spring-integration.leader.hazelcast.enabled", matchIfMissing = true)
@EnableConfigurationProperties(LeaderConfigurationProperties.class)
public class HazelcastLeaderAutoConfiguration {

    @Bean
    public LeaderProviderMarker providerMarker() {
        return new LeaderProviderMarker();
    }

    @Bean
    @ConditionalOnMissingBean(LeaderInitiator.class)
    public static LeaderInitiatorPostProcessor hazelcastLeaderInitiatorPostProcessor() {
        return new LeaderInitiatorPostProcessor();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor hazelcastLeaderProviderPostProcessor() {
        return new LeaderProviderPostProcessor<>(LeaderInitiator.class, HazelcastLeaderProvider.class);
    }

    private static class LeaderInitiatorPostProcessor extends AbstractLeaderInitiatorPostProcessor {

        @Override
        protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher) {
            HazelcastInstance hazelcastInstance = beanFactory.getBean(HazelcastInstance.class);
            DefaultCandidate candidate = new DefaultCandidate(UUID.randomUUID().toString(), role);
            return BeanDefinitionBuilder.genericBeanDefinition(LeaderInitiator.class)
                    .addConstructorArgValue(hazelcastInstance)
                    .addConstructorArgValue(candidate)
                    .addPropertyValue("leaderEventPublisher", new DefaultLeaderEventPublisher(applicationEventPublisher))
                    .getBeanDefinition();
        }

    }

    static class HazelcastLeaderProvider implements LeaderProvider {

        private final LeaderInitiator leaderInitiator;

        HazelcastLeaderProvider(LeaderInitiator leaderInitiator) {
            this.leaderInitiator = leaderInitiator;
        }

        @Override
        public Context context() {
            return leaderInitiator.getContext();
        }
    }

}
