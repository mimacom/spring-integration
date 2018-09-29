package com.mimacom.spring.integration.leader;


import javax.sql.DataSource;

import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "spring-integration.leader.zookeeper.enabled=false",
        "spring-integration.leader.type=lock-registry"
})
public class LockRegistryLeaderAwareAutoConfigurationTest extends AbstractLeaderAwareAutoConfigurationTest {

    @Configuration
    @EnableAutoConfiguration
    static class TestConfigUsingLockRegistry {

        @Bean
        public LockRegistry lockRegistry(LockRepository lockRepository) {
            return new JdbcLockRegistry(lockRepository);
        }

        @Bean
        public LockRepository lockRepository(DataSource dataSource) {
            return new DefaultLockRepository(dataSource);
        }

    }
}
