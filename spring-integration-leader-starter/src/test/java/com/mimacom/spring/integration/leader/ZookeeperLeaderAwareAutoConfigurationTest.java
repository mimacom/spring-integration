package com.mimacom.spring.integration.leader;

import static org.assertj.core.api.Assertions.assertThat;

import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import org.apache.curator.test.TestingServer;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.zookeeper.config.CuratorFrameworkFactoryBean;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @Autowired(required = false)
    private LeaderInitiator leaderInitiator;

    @Test
    public void testZookeeperBasedLeaderInitiator() {
        assertThat(leaderInitiator).isNotNull();
    }

    @SpringBootApplication(exclude = HazelcastLeaderAutoConfiguration.class)
    static class TestConfigUsingZookeeper {

        @Bean
        CuratorFrameworkFactoryBean curatorFramework() throws Exception {
            return new CuratorFrameworkFactoryBean((this.testingServer().getConnectString()));
        }

        @Bean(destroyMethod = "close")
        TestingServer testingServer() throws Exception {
            return new TestingServer();
        }

    }

}