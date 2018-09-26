package com.mimacom.spring.integration.leader.providers;

import static com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorPostProcessor.LEADER_INITIATOR_BEAN_NAME_POSTFIX;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.util.Assert;

public class LeaderProviderPostProcessor<T, P> implements BeanDefinitionRegistryPostProcessor {

    private static final String LEADER_PROVIDER_BEAN_NAME_POSTFIX = "_leaderProvider";

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
        beanFactory.getBeansOfType(leaderInitiatorClazz)
                .forEach((leaderInitiatorBeanName, leaderInitiator) ->
                        registry.registerBeanDefinition(
                                leaderProviderBeanName(leaderInitiatorBeanName),
                                leaderProviderBeanDefinition(leaderInitiator)
                        ));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // nothing to do
    }

    private BeanDefinition leaderProviderBeanDefinition(T leaderInitiator) {
        return BeanDefinitionBuilder.genericBeanDefinition(leaderProviderClazz)
                .addConstructorArgValue(leaderInitiator)
                .getBeanDefinition();
    }

    private String leaderProviderBeanName(String leaderInitiatorBeanName) {
        if (leaderInitiatorBeanName.contains(LEADER_INITIATOR_BEAN_NAME_POSTFIX)) {
            return leaderInitiatorBeanName.replace(LEADER_INITIATOR_BEAN_NAME_POSTFIX, LEADER_PROVIDER_BEAN_NAME_POSTFIX);
        } else {
            return (counter++) + LEADER_PROVIDER_BEAN_NAME_POSTFIX;
        }
    }
}
