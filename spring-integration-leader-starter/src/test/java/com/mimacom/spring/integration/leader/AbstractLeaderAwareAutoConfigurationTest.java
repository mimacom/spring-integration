package com.mimacom.spring.integration.leader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.mimacom.spring.integration.leader.actuator.LeaderHealthIndicator;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private LeaderAwareEndpointPostProcessor leaderAwareEndpointPostProcessor;

    @Autowired(required = false)
    private LeaderProvider leaderProvider;

    @Autowired(required = false)
    private LeaderHealthIndicator leaderHealthIndicator;

    @Test
    public void testLeaderAwareIntegrationAutoConfiguration() {
        assertThat(leaderAwareEndpointPostProcessor).isNotNull();
        assertThat(leaderProvider).isNotNull();
        await().untilAsserted(() -> assertThat(this.leaderProvider.context().isLeader()).isTrue());
        assertThat(leaderHealthIndicator).isNotNull();
    }

}
