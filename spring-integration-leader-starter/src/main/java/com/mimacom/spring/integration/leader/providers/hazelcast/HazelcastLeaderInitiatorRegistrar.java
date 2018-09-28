package com.mimacom.spring.integration.leader.providers.hazelcast;

import java.util.UUID;

import com.hazelcast.core.HazelcastInstance;
import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorRegistrar;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.integration.leader.DefaultCandidate;

class HazelcastLeaderInitiatorRegistrar extends AbstractLeaderInitiatorRegistrar {

    @Override
    protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role) {
        return BeanDefinitionBuilder.genericBeanDefinition(HazelcastLeaderInitiatorFactoryBean.class)
                .addConstructorArgValue(beanFactory.getBean(HazelcastInstance.class))
                .addConstructorArgValue(new DefaultCandidate(UUID.randomUUID().toString(), role))
                .getBeanDefinition();
    }

}
