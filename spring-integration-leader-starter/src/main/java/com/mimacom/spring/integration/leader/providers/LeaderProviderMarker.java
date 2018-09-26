package com.mimacom.spring.integration.leader.providers;

public class LeaderProviderMarker {

    private final String type;

    public LeaderProviderMarker(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
