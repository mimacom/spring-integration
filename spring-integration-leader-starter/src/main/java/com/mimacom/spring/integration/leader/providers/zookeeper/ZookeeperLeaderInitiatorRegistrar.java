package com.mimacom.spring.integration.leader.providers.zookeeper;

import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorRegistrar;
import org.apache.curator.framework.CuratorFramework;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.integration.zookeeper.config.LeaderInitiatorFactoryBean;

class ZookeeperLeaderInitiatorRegistrar extends AbstractLeaderInitiatorRegistrar {

    @Override
    protected BeanDefinition leaderInitiatorBeanDefinition(BeanFactory beanFactory, String role) {
        CuratorFramework curatorFramework = beanFactory.getBean(CuratorFramework.class);
        String path = zookeeperLeaderConfigurationProperties().getPath();
        return BeanDefinitionBuilder.genericBeanDefinition(ZookeeperLeaderInitiatorFactoryBeanAdapter.class)
                .addConstructorArgValue(curatorFramework)
                .addConstructorArgValue(role)
                .addConstructorArgValue(path)
                .getBeanDefinition();
    }

    private ZookeeperLeaderConfigurationProperties zookeeperLeaderConfigurationProperties() {
        return Binder.get(environment)
                .bind("spring-integration.leader.zookeeper", Bindable.of(ZookeeperLeaderConfigurationProperties.class))
                .orElseThrow(() -> new IllegalStateException("ZookeeperLeaderConfigurationProperties not available"));
    }

    static class ZookeeperLeaderInitiatorFactoryBeanAdapter extends LeaderInitiatorFactoryBean {

        public ZookeeperLeaderInitiatorFactoryBeanAdapter(CuratorFramework client, String role, String path) {
            super();
            super.setClient(client)
                    .setRole(role)
                    .setPath(path);
        }


    }
}
