package com.mimacom.spring.integration.leader.providers;

import static com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorRegistrar.LEADER_INITIATOR_BEAN_NAME_POSTFIX;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

public class LeaderProviderRegistrar<T, P> implements ImportBeanDefinitionRegistrar {

    private static final String LEADER_PROVIDER_BEAN_NAME_POSTFIX = "_leaderProvider";

    private int counter = 0;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ResolvableType resolvableType = ResolvableType.forClass(LeaderProviderRegistrar.class, this.getClass());
        Class<T> leaderInitiatorClazz = (Class<T>) resolvableType.getGeneric(0).getType();
        Class<P> leaderProviderClazz = (Class<P>) resolvableType.getGeneric(1).getType();
        Assert.isInstanceOf(ConfigurableListableBeanFactory.class, registry);
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        beanFactory.getBeansOfType(leaderInitiatorClazz)
                .forEach((leaderInitiatorBeanName, leaderInitiator) ->
                        registry.registerBeanDefinition(
                                leaderProviderBeanName(leaderInitiatorBeanName),
                                leaderProviderBeanDefinition(leaderInitiator, leaderProviderClazz)
                        ));
    }

    private BeanDefinition leaderProviderBeanDefinition(T leaderInitiator, Class<P> leaderProviderClazz) {
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
