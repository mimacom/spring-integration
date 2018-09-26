package com.mimacom.spring.integration.leader.providers;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.util.Assert;

public abstract class AbstractLeaderInitiatorPostProcessor implements ApplicationEventPublisherAware, BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationEventPublisher applicationEventPublisher;

    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, registry, "Zookeeper Leader Auto-configuration can only be used with a ConfigurableListableBeanFactory");
        ConfigurableListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory) registry;
        Binder binder = Binder.get(this.applicationContext.getEnvironment());

        BindResult<List<String>> bind = binder.bind("spring-integration.leader-aware.roles", Bindable.listOf(String.class));
        bind.get().forEach((String role) -> registry.registerBeanDefinition(
                role + "_leaderInitiator",
                leaderInitiatorBeanDefinition(
                        configurableListableBeanFactory,
                        role,
                        applicationEventPublisher)
                )
        );
    }

    protected abstract BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role, ApplicationEventPublisher applicationEventPublisher);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
