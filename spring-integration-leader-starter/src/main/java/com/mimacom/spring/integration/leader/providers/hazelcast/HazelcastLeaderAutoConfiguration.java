package com.mimacom.spring.integration.leader.providers.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderTypeCondition;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(HazelcastAutoConfiguration.class)
@Import({HazelcastLeaderInitiatorRegistrar.class, HazelcastLeaderProviderRegistrar.class})
@ConditionalOnSingleCandidate(HazelcastInstance.class)
@ConditionalOnMissingBean(LeaderProvider.class)
@Conditional(LeaderProviderTypeCondition.class)
public class HazelcastLeaderAutoConfiguration {

}
