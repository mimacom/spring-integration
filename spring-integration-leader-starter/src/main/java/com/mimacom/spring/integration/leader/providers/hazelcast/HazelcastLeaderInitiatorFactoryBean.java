package com.mimacom.spring.integration.leader.providers.hazelcast;

import com.hazelcast.core.HazelcastInstance;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.integration.hazelcast.leader.LeaderInitiator;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;

class HazelcastLeaderInitiatorFactoryBean implements FactoryBean<LeaderInitiator>, SmartLifecycle, ApplicationEventPublisherAware, InitializingBean {

    private ApplicationEventPublisher applicationEventPublisher;

    private HazelcastInstance hazelcastInstance;

    private Candidate candidate;

    private LeaderInitiator leaderInitiator;

    private boolean autoStartup = true;

    private int phase = Integer.MAX_VALUE - 1000;

    public HazelcastLeaderInitiatorFactoryBean(HazelcastInstance hazelcastInstance, Candidate candidate) {
        this.candidate = candidate;
        this.hazelcastInstance = hazelcastInstance;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    public LeaderInitiator getObject() {
        return leaderInitiator;
    }

    @Override
    public Class<LeaderInitiator> getObjectType() {
        return LeaderInitiator.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        if (this.leaderInitiator == null) {
            this.leaderInitiator = new LeaderInitiator(this.hazelcastInstance, this.candidate);
            this.leaderInitiator.setPhase(this.phase);
            this.leaderInitiator.setAutoStartup(this.autoStartup);
            this.leaderInitiator.setLeaderEventPublisher(new DefaultLeaderEventPublisher(this.applicationEventPublisher));
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public boolean isAutoStartup() {
        return this.leaderInitiator != null && this.leaderInitiator.isAutoStartup();
    }

    @Override
    public void start() {
        if (this.leaderInitiator != null) {
            this.leaderInitiator.start();
        }
    }

    @Override
    public void stop() {
        if (this.leaderInitiator != null) {
            this.leaderInitiator.stop();
        }
    }

    @Override
    public void stop(Runnable callback) {
        if (this.leaderInitiator != null) {
            this.leaderInitiator.stop(callback);
        } else {
            callback.run();
        }
    }

    @Override
    public boolean isRunning() {
        return this.leaderInitiator != null && this.leaderInitiator.isRunning();
    }

    @Override
    public int getPhase() {
        if (this.leaderInitiator != null) {
            return this.leaderInitiator.getPhase();
        }
        return this.phase;
    }

}
