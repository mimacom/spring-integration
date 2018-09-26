package com.mimacom.spring.integration.leader;

import static com.mimacom.spring.integration.leader.LeaderAwareEndpointPostProcessorTest.SAMPLE_ENDPOINT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.leader.Context;
import org.springframework.integration.leader.event.OnGrantedEvent;
import org.springframework.integration.leader.event.OnRevokedEvent;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring-integration.leader.endpoints=" + SAMPLE_ENDPOINT_NAME})
public class LeaderAwareEndpointPostProcessorTest {

    static final String SAMPLE_ENDPOINT_NAME = "SAMPLE_ENDPOINT_NAME";

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

    @SpringBootApplication(exclude = {
            org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration.class,
            com.mimacom.spring.integration.zookeeper.ZookeeperAutoConfiguration.class
    })
    static class TestConfig {

        @Bean
        LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor() {
            return new LeaderAwareEndpointPostProcessor(DEFAULT_ROLE, Collections.singletonList(SAMPLE_ENDPOINT_NAME));
        }

        @Bean
        IntegrationFlow sampleIntegrationFlow() {
            return IntegrationFlows
                    .from(() -> UUID.randomUUID().toString(), c -> c
                            .id(SAMPLE_ENDPOINT_NAME)
                            .poller(p -> p.fixedDelay(1000))
                            .autoStartup(true))
                    .handle((GenericHandler<String>) (payload, headers) -> null)
                    .get();
        }
    }
}