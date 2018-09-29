package com.mimacom.spring.integration.leader.providers.lockregistry;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderTypeCondition;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.jdbc.lock.LockRepository;

@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@Import({LockRegistryLeaderInitiatorRegistrar.class, LockRegistryLeaderProviderRegistrar.class})
@ConditionalOnSingleCandidate(LockRepository.class)
@ConditionalOnMissingBean(LeaderProvider.class)
@Conditional(LeaderProviderTypeCondition.class)
public class LockRegistryLeaderAutoConfiguration {

}
