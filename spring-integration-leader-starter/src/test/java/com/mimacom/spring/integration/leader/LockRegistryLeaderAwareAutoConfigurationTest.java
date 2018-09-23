package com.mimacom.spring.integration.leader;


import javax.sql.DataSource;

import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.zookeeper.ZookeeperAutoConfiguration;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LockRegistryLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @SpringBootApplication(exclude = {
            HazelcastLeaderAutoConfiguration.class,
            ZookeeperAutoConfiguration.class,
            org.springframework.cloud.zookeeper.ZookeeperAutoConfiguration.class
    })
    static class TestConfigUsingLockRegistry {

        private final DataSource dataSource;

        TestConfigUsingLockRegistry(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        public LockRegistry lockRegistry() {
            return new JdbcLockRegistry(this.lockRepository());
        }

        @Bean
        public DefaultLockRepository lockRepository() {
            return new DefaultLockRepository(this.dataSource);
        }

        @Bean
        public LockRegistryLeaderInitiator leaderInitiator() {
            LockRegistryLeaderInitiator lockRegistryLeaderInitiator = new LockRegistryLeaderInitiator(this.lockRegistry());
            lockRegistryLeaderInitiator.start();
            return lockRegistryLeaderInitiator;
        }

    }
}
