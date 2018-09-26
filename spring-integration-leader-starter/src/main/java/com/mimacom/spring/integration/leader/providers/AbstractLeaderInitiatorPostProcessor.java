package com.mimacom.spring.integration.leader.providers;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;

public abstract class AbstractLeaderInitiatorPostProcessor implements ApplicationEventPublisherAware, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    public static final String LEADER_INITIATOR_BEAN_NAME_POSTFIX = "_leaderInitiator";

    private ApplicationEventPublisher applicationEventPublisher;

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Assert.isInstanceOf(BeanFactory.class, registry, "AbstractLeaderInitiatorPostProcessor can only be used with a BeanFactory");
        BeanFactory beanFactory = (BeanFactory) registry;
        Binder binder = Binder.get(this.applicationContext.getEnvironment());
        binder.bind("spring-integration.leader-aware.roles", Bindable.listOf(String.class))
                .ifBound(roles -> roles.forEach(role -> {
                    BeanDefinition leaderInitiatorBeanDefinition = leaderInitiatorBeanDefinition(beanFactory, role, applicationEventPublisher);
                    registry.registerBeanDefinition(role + LEADER_INITIATOR_BEAN_NAME_POSTFIX, leaderInitiatorBeanDefinition);
                }));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing to do
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    protected abstract BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher);

}
