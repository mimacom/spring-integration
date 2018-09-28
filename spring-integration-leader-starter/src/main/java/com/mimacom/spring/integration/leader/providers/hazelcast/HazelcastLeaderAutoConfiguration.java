package com.mimacom.spring.integration.leader.providers.hazelcast;

import com.hazelcast.core.HazelcastInstance;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {
        HazelcastLeaderInitiatorRegistrar.class,
        HazelcastLeaderProviderRegistrar.class}
)
@ConditionalOnBean(HazelcastInstance.class)
@AutoConfigureAfter(HazelcastAutoConfiguration.class)
@ConditionalOnProperty(value = "spring-integration.leader.hazelcast.enabled", matchIfMissing = true)
public class HazelcastLeaderAutoConfiguration {

}
