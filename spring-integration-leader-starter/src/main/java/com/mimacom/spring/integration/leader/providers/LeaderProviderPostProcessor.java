package com.mimacom.spring.integration.leader.providers;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.Assert;

public class LeaderProviderPostProcessor<T, P> implements BeanDefinitionRegistryPostProcessor {

    private final Class<T> leaderInitiatorClazz;

    private final Class<P> leaderProviderClazz;

    private int counter = 0;

    public LeaderProviderPostProcessor(Class<T> leaderInitiatorClazz, Class<P> leaderProviderClazz) {
        this.leaderInitiatorClazz = leaderInitiatorClazz;
        this.leaderProviderClazz = leaderProviderClazz;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, registry);
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        Map<String, T> beansOfType = beanFactory.getBeansOfType(leaderInitiatorClazz);
        for (Map.Entry<String, T> entry : beansOfType.entrySet()) {
            T leaderInitiator = entry.getValue();
            String role = leaderProviderBeanName(entry.getKey());
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(leaderProviderClazz)
                    .addConstructorArgValue(leaderInitiator)
                    .getBeanDefinition();
            registry.registerBeanDefinition(role + "leaderProvider", beanDefinition);
        }
    }

    public String leaderProviderBeanName(String leaderInitiatorBeanName) {
        if (leaderInitiatorBeanName.contains("leaderInitiator")) {
            return leaderInitiatorBeanName.replace("leaderInitiator", "leaderProvider");
        } else {
            return "leaderProvider_" + counter++;
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
