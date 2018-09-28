package com.mimacom.spring.integration.leader.providers.lockregistry;

import com.mimacom.spring.integration.leader.providers.LeaderProvider;
import com.mimacom.spring.integration.leader.providers.LeaderProviderRegistrar;

import org.springframework.integration.leader.Context;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;

class LockRegistryLeaderProviderRegistrar extends LeaderProviderRegistrar<LockRegistryLeaderInitiator, LockRegistryLeaderProviderRegistrar.LockRegistryLeaderProvider> {

    static class LockRegistryLeaderProvider implements LeaderProvider {

        private final LockRegistryLeaderInitiator lockRegistryLeaderInitiator;

        private LockRegistryLeaderProvider(LockRegistryLeaderInitiator lockRegistryLeaderInitiator) {
            this.lockRegistryLeaderInitiator = lockRegistryLeaderInitiator;
        }

        @Override
        public Context context() {
            return lockRegistryLeaderInitiator.getContext();
        }
    }
}
