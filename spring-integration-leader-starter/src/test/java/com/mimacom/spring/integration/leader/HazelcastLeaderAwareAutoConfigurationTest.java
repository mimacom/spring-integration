package com.mimacom.spring.integration.leader;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.DefaultCandidate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.hazelcast.config=classpath:config/my-hazelcast.xml"})
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
    @EnableAutoConfiguration(exclude = {
            org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration.class,
            com.mimacom.spring.integration.zookeeper.ZookeeperAutoConfiguration.class
    })
    static class TestConfigUsingHazelcast {

        @Bean
        public LeaderInitiator leaderInitiator(HazelcastInstance hazelcastInstance) {
            DefaultCandidate candidate = new DefaultCandidate(UUID.randomUUID().toString(), "xxx");
            return new LeaderInitiator(hazelcastInstance, candidate);
        }

    }
}
