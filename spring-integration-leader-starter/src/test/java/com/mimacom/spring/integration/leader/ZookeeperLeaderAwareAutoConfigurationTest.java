package com.mimacom.spring.integration.leader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.mimacom.spring.integration.leader.providers.AbstractLeaderInitiatorRegistrar;
import org.apache.curator.test.TestingServer;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.zookeeper.ZookeeperProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "spring-integration.leader.type=zookeeper"
})
public class ZookeeperLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private List<LeaderInitiator> leaderInitiator;

    @Autowired
    @Qualifier("test-role-1" + AbstractLeaderInitiatorRegistrar.LEADER_INITIATOR_BEAN_NAME_POSTFIX)
    private LeaderInitiator leaderInitiatorOne;

    @Test
    public void testZookeeperBasedLeaderInitiator() {
        assertThat(leaderInitiator)
                .isNotEmpty()
                .hasOnlyElementsOfType(LeaderInitiator.class);

        assertThat(this.leaderInitiatorOne).isNotNull();
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableScheduling
    static class TestConfigUsingZookeeper {

        @Bean
        ZookeeperProperties zookeeperProperties() throws Exception {
            ZookeeperProperties zookeeperProperties = new ZookeeperProperties();
            zookeeperProperties.setConnectString(this.testingServer().getConnectString());
            return zookeeperProperties;
        }

        @Bean(destroyMethod = "close")
        TestingServer testingServer() throws Exception {
            return new TestingServer();
        }

    }

}