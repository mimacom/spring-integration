package com.mimacom.spring.integration.leader.providers.hazelcast;

import java.util.UUID;

import com.hazelcast.core.HazelcastInstance;
import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorPostProcessor;
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
public class HazelcastLeaderAutoConfiguration {

    private static final String PROVIDER_TYPE_NAME = "hazelcast";

    @Bean
    public LeaderProviderMarker leaderProviderMarker() {
        return new LeaderProviderMarker(PROVIDER_TYPE_NAME);
    }

    @Bean
    @ConditionalOnMissingBean(LeaderInitiator.class)
    public static HazelcastLeaderInitiatorPostProcessor hazelcastLeaderInitiatorPostProcessor() {
        return new HazelcastLeaderInitiatorPostProcessor();
    }

    @Bean
    public static BeanDefinitionRegistryPostProcessor hazelcastLeaderProviderPostProcessor() {
        return new LeaderProviderPostProcessor<>(LeaderInitiator.class, HazelcastLeaderProvider.class);
    }

    private static class HazelcastLeaderInitiatorPostProcessor extends AbstractLeaderInitiatorPostProcessor {

        @Override
        protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher) {
            return BeanDefinitionBuilder.genericBeanDefinition(LeaderInitiator.class)
                    .addConstructorArgValue(beanFactory.getBean(HazelcastInstance.class))
                    .addConstructorArgValue(new DefaultCandidate(UUID.randomUUID().toString(), role))
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
