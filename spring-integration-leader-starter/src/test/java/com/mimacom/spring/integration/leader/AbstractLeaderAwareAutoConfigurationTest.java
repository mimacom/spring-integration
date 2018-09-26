package com.mimacom.spring.integration.leader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.UUID;

import com.mimacom.spring.integration.leader.actuator.LeaderHealthIndicator;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@Import(AbstractLeaderAwareAutoConfigurationTest.TestFlowConfig.class)
@ActiveProfiles("test")
public abstract class AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor;

    @Autowired(required = false)
    private List<LeaderProvider> leaderProviders;

    @Autowired(required = false)
    private LeaderHealthIndicator leaderHealthIndicator;

    @Autowired
    @Qualifier("testFlow")
    private AbstractEndpoint testFlow;

    @Test
    public void testLeaderAwareIntegrationAutoConfiguration() {
        assertThat(leaderAwareEndpointPostProcessor).isNotNull();
        assertThat(leaderProviders).isNotEmpty();
        assertThat(leaderHealthIndicator).isNotNull();
        await()
                .untilAsserted(() -> assertThat(testFlow.isRunning()).isTrue());
    }

    @Configuration
    static class TestFlowConfig {

        @Bean
        IntegrationFlow sampleIntegrationFlow() {
            return IntegrationFlows
                    .from(() -> UUID.randomUUID().toString(), c -> c
                            .id("testFlow")
                            .role("test-role-2")
                            .poller(p -> p.fixedDelay(1000))
                            .autoStartup(false))
                    .handle((GenericHandler<String>) (payload, headers) -> null)
                    .get();
        }
    }
}


