package com.mimacom.spring.integration.leader.providers;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public abstract class AbstractLeaderInitiatorRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    static final String LEADER_INITIATOR_BEAN_NAME_POSTFIX = "_leaderInitiator";

    protected Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Assert.isInstanceOf(BeanFactory.class, registry, "AbstractLeaderInitiatorPostProcessor can only be used with a BeanFactory");
        BeanFactory beanFactory = (BeanFactory) registry;
        Binder.get(this.environment)
                .bind("spring-integration.leader.roles", Bindable.listOf(String.class))
                .ifBound(roles -> roles.forEach(role -> {
                    BeanDefinition leaderInitiatorBeanDefinition = leaderInitiatorBeanDefinition(beanFactory, role);
                    registry.registerBeanDefinition(role + LEADER_INITIATOR_BEAN_NAME_POSTFIX, leaderInitiatorBeanDefinition);
                }));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    protected abstract BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role);

}
