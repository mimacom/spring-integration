package com.mimacom.spring.integration.leader.providers.hazelcast;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderRegistrar;

import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Context;

class HazelcastLeaderProviderRegistrar extends LeaderProviderRegistrar<LeaderInitiator, HazelcastLeaderProviderRegistrar.HazelcastLeaderProvider> {

    static class HazelcastLeaderProvider implements LeaderProvider {

        private final LeaderInitiator leaderInitiator;

        HazelcastLeaderProvider(LeaderInitiator leaderInitiator) {
            this.leaderInitiator = leaderInitiator;
        }

        @Override
        public Context context() {
            return leaderInitiator.getContext();
        }
    }
}
