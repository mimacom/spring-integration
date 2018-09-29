package com.mimacom.spring.integration.leader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LeaderAwareEndpointRolePostProcessorTest {

    private static final String SAMPLE_ENDPOINT_NAME = "SAMPLE_ENDPOINT_NAME";

    private static final String DEFAULT_ROLE = "default-role";

    @Autowired
    @Qualifier(SAMPLE_ENDPOINT_NAME)
    private AbstractEndpoint testingEndpoint;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    public void testOnGrantedEvent() {
        // given
        assertThat(testingEndpoint.isRunning()).isFalse();

        // when
        applicationEventPublisher.publishEvent(new OnGrantedEvent(new Object(), mock(Context.class), DEFAULT_ROLE));

        // then
        assertThat(testingEndpoint.isRunning()).isTrue();
    }

    @Test
    public void testOnRevokedEvent() {
        // given
        testingEndpoint.start();

        // when
        applicationEventPublisher.publishEvent(new OnRevokedEvent(new Object(), mock(Context.class), DEFAULT_ROLE));

        // then
        assertThat(testingEndpoint.isRunning()).isFalse();
    }

    @Configuration
    @EnableIntegration
    static class TestConfig {

        @Bean
        LeaderAwareEndpointRolePostProcessor leaderAwareEndpointPostProcessor() {
            return new LeaderAwareEndpointRolePostProcessor(Collections.singletonList(DEFAULT_ROLE));
        }

        @Bean
        IntegrationFlow sampleIntegrationFlow() {
            return IntegrationFlows
                    .from(() -> UUID.randomUUID().toString(), c -> c
                            .id(SAMPLE_ENDPOINT_NAME)
                            .role(DEFAULT_ROLE)
                            .poller(p -> p.fixedDelay(1000))
                            .autoStartup(true))
                    .handle((GenericHandler<String>) (payload, headers) -> null)
                    .get();
        }
    }
}