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

public class LeaderAwareEndpointPostProcessor implements BeanPostProcessor, ApplicationListener<AbstractLeaderEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderAwareEndpointPostProcessor.class);

    private final List<String> pollingEndpointBeanNames = new ArrayList<>();

    private List<AbstractEndpoint> endpoints = new ArrayList<>();

    public LeaderAwareEndpointPostProcessor(List<String> pollingEndpointBeanNames) {
        this.pollingEndpointBeanNames.addAll(pollingEndpointBeanNames);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof AbstractEndpoint)) {
            return bean;
        }
        if (!this.pollingEndpointBeanNames.contains(beanName)) {
            LOGGER.warn("'{}' bean is not configured to become leader-aware", beanName);
            return bean;
        }
        LOGGER.info("'{}' bean is going to be configured leader-aware", beanName);
        AbstractEndpoint pollingEndpoint = (AbstractEndpoint) bean;
        pollingEndpoint.setAutoStartup(false);
        this.endpoints.add(pollingEndpoint);
        return bean;
    }

    @Override
    public void onApplicationEvent(AbstractLeaderEvent event) {
        LOGGER.info("Received Leader Event: {}", event);
        if (event instanceof OnGrantedEvent) {
            this.endpoints.forEach(AbstractEndpoint::start);
        } else if (event instanceof OnRevokedEvent) {
            this.endpoints.forEach(AbstractEndpoint::stop);
        }
    }
}
