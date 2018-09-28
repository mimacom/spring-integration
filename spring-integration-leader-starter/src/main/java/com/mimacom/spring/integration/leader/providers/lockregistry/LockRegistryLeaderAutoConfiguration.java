package com.mimacom.spring.integration.leader.providers.lockregistry;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.jdbc.lock.LockRepository;

@Configuration
@Import({LockRegistryLeaderInitiatorRegistrar.class, LockRegistryLeaderProviderRegistrar.class})
@ConditionalOnBean(LockRepository.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(value = "spring-integration.leader.lock-registry.enabled", matchIfMissing = true)
public class LockRegistryLeaderAutoConfiguration {

}
