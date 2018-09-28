package com.mimacom.spring.integration.leader.providers.lockregistry;

import java.util.UUID;

import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorRegistrar;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.integration.support.locks.LockRegistry;


class LockRegistryLeaderInitiatorRegistrar extends AbstractLeaderInitiatorRegistrar {

    @Override
    protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role) {
        LockRegistry lockRegistry = beanFactory.getBean(LockRegistry.class);
        return BeanDefinitionBuilder.genericBeanDefinition(LockRegistryLeaderInitiatorFactoryBean.class)
                .addConstructorArgValue(lockRegistry)
                .addConstructorArgValue(new DefaultCandidate(UUID.randomUUID().toString(), role))
                .getBeanDefinition();
    }
}
