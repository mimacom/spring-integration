package com.mimacom.spring.integration.leader;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.leader.event.AbstractLeaderEvent;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.util.Assert;

public class LeaderAwareEndpointPostProcessor implements BeanPostProcessor, ApplicationListener<AbstractLeaderEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderAwareEndpointPostProcessor.class);

    private final List<String> leaderAwareEndpointNames = new ArrayList<>();

    private List<AbstractEndpoint> endpoints = new ArrayList<>();

    private String role;

    LeaderAwareEndpointPostProcessor(String role, List<String> leaderAwareEndpointNames) {
        Assert.hasText(role, "Role must not be empty");
        this.role = role;
        if (leaderAwareEndpointNames != null) {
            this.leaderAwareEndpointNames.addAll(leaderAwareEndpointNames);
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof AbstractEndpoint)) {
            return bean;
        }
        if (!this.leaderAwareEndpointNames.contains(beanName)) {
            LOGGER.debug("'{}' bean is not configured to become leader-aware", beanName);
            return bean;
        }
        LOGGER.info("'{}' spring-integration endpoint is going to be configured leader-aware for role: ", beanName, this.role);
        AbstractEndpoint endpoint = (AbstractEndpoint) bean;
        endpoint.setAutoStartup(false);
        this.endpoints.add(endpoint);
        return bean;
    }

    @Override
    public void onApplicationEvent(AbstractLeaderEvent event) {
        LOGGER.info("Received Leader Event: {}", event);
        if (event instanceof OnGrantedEvent && event.getRole().equalsIgnoreCase(this.role)) {
            this.endpoints.forEach(AbstractEndpoint::start);
        } else if (event instanceof OnRevokedEvent && event.getRole().equalsIgnoreCase(this.role)) {
            this.endpoints.forEach(AbstractEndpoint::stop);
        }
    }
}
