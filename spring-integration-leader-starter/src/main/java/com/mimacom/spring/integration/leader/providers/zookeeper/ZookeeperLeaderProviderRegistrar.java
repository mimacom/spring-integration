package com.mimacom.spring.integration.leader.providers.zookeeper;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderRegistrar;

import org.springframework.integration.leader.Context;
import org.springframework.integration.zookeeper.leader.LeaderInitiator;

class ZookeeperLeaderProviderRegistrar extends LeaderProviderRegistrar<LeaderInitiator, ZookeeperLeaderProviderRegistrar.ZookeeperLeaderProvider> {

    static class ZookeeperLeaderProvider implements LeaderProvider {

        private final LeaderInitiator leaderInitiator;

        ZookeeperLeaderProvider(LeaderInitiator leaderInitiator) {
            this.leaderInitiator = leaderInitiator;
        }

        @Override
        public Context context() {
            return leaderInitiator.getContext();
        }
    }

}
