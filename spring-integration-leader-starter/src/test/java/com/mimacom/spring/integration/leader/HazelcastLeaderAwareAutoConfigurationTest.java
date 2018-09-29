package com.mimacom.spring.integration.leader;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "spring-integration.leader.zookeeper.enabled=false",
        "spring.hazelcast.config=classpath:config/my-hazelcast.xml",
        "spring-integration.leader.type=hazelcast"
})
public class HazelcastLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private List<LeaderInitiator> leaderInitiator;

    @Test
    public void testHazelcastBasedLeaderInitiator() {
        assertThat(leaderInitiator)
                .isNotEmpty()
                .hasOnlyElementsOfType(LeaderInitiator.class);
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfigUsingHazelcast {


    }
}
