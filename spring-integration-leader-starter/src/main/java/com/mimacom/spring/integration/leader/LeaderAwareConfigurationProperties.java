package com.mimacom.spring.integration.leader;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("spring-integration.leader-aware")
@Validated
public class LeaderAwareConfigurationProperties {

    @NotNull
    private List<String> endpoints = new ArrayList<>();

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }
}
