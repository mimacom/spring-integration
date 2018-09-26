package com.mimacom.spring.integration.leader.providers.lockregistry;

import java.util.UUID;

import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorPostProcessor;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderMarker;
import com.mimacom.spring.integration.leader.providers.LeaderProviderPostProcessor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.LockRegistry;

@Configuration
@ConditionalOnBean(LockRepository.class)
@ConditionalOnProperty(value = "spring-integration.leader.lock-registry.enabled", matchIfMissing = true)
public class LockRegistryLeaderAutoConfiguration {

    @Bean
    public LeaderProviderMarker providerMarker() {
        return new LeaderProviderMarker();
    }

    @Bean
    @ConditionalOnMissingBean(LockRegistryLeaderInitiator.class)
    public static LeaderInitiatorPostProcessor lockRegistryLeaderInitiatorPostProcessor() {
        return new LeaderInitiatorPostProcessor();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor lockRegistryLeaderProviderPostProcessor() {
        return new LeaderProviderPostProcessor<>(LockRegistryLeaderInitiator.class, LockRegistryLeaderProvider.class);
    }

    private static class LeaderInitiatorPostProcessor extends AbstractLeaderInitiatorPostProcessor {

        @Override
        protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher) {
            LockRegistry lockRegistry = beanFactory.getBean(LockRegistry.class);
            DefaultCandidate candidate = new DefaultCandidate(UUID.randomUUID().toString(), role);
            return BeanDefinitionBuilder.genericBeanDefinition(LockRegistryLeaderInitiator.class)
                    .addConstructorArgValue(lockRegistry)
                    .addConstructorArgValue(candidate)
                    .addPropertyValue("leaderEventPublisher", new DefaultLeaderEventPublisher(applicationEventPublisher))
                    .getBeanDefinition();
        }

    }

    private static class LockRegistryLeaderProvider implements LeaderProvider {

        private final LockRegistryLeaderInitiator lockRegistryLeaderInitiator;

        private LockRegistryLeaderProvider(LockRegistryLeaderInitiator lockRegistryLeaderInitiator) {
            this.lockRegistryLeaderInitiator = lockRegistryLeaderInitiator;
        }

        @Override
        public Context context() {
            return lockRegistryLeaderInitiator.getContext();
        }
    }
}
