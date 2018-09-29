package com.mimacom.spring.integration.leader.providers;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import com.mimacom.spring.integration.leader.providers.hazelcast.HazelcastLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.lockregistry.LockRegistryLeaderAutoConfiguration;
import com.mimacom.spring.integration.leader.providers.zookeeper.ZookeeperLeaderAutoConfiguration;

public class LeaderProviderConfiguration {

    private static final Map<LeaderProviderType, Class<?>> MAPPINGS;

    static {
        Map<LeaderProviderType, Class<?>> mappings = new EnumMap<>(LeaderProviderType.class);
        mappings.put(LeaderProviderType.ZOOKEEPER, ZookeeperLeaderAutoConfiguration.class);
        mappings.put(LeaderProviderType.HAZELCAST, HazelcastLeaderAutoConfiguration.class);
        mappings.put(LeaderProviderType.LOCK_REGISTRY, LockRegistryLeaderAutoConfiguration.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private LeaderProviderConfiguration() {
    }

    static LeaderProviderType getType(String configurationClassName) {
        for (Map.Entry<LeaderProviderType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }

}
