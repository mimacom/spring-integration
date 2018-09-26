package com.mimacom.spring.integration.leader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.integration.endpoint.AbstractEndpoint;

public class LeaderAwareEndpointRolePostProcessor implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderAwareEndpointRolePostProcessor.class);

    private List<String> leaderAwareRoles = new ArrayList<>();

    LeaderAwareEndpointRolePostProcessor(List<String> leaderAwareRoles) {
        if (leaderAwareRoles != null) {
            this.leaderAwareRoles.addAll(leaderAwareRoles);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof AbstractEndpoint)) {
            return bean;
        }
        AbstractEndpoint endpoint = (AbstractEndpoint) bean;
        if (this.leaderAwareRoles.contains(endpoint.getRole())) {
            if (endpoint.isAutoStartup()) {
                LOGGER.warn("'{}' spring-integration endpoint has a leader-aware role '{}' and has auto-start set to true -> disabling auto-start ", beanName, endpoint.getRole());
                endpoint.setAutoStartup(false);
            }
        }
        return bean;
    }

}
