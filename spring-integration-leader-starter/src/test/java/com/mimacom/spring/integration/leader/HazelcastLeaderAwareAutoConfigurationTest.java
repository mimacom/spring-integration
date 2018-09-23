package com.mimacom.spring.integration.leader;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.hazelcast.config=classpath:config/my-hazelcast.xml"})
public class HazelcastLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private LeaderInitiator leaderInitiator;

    @Test
    public void testHazelcastBasedLeaderInitiator() {
        assertThat(leaderInitiator).isNotNull();
    }

    @SpringBootApplication(exclude = {
            org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration.class,
            com.mimacom.spring.integration.zookeeper.ZookeeperAutoConfiguration.class
    })
    static class TestConfigUsingHazelcast {

    }
}
