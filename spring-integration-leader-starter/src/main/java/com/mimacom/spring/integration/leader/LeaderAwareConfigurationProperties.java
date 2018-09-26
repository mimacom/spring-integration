package com.mimacom.spring.integration.leader;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("spring-integration.leader-aware")
@Validated
public class LeaderAwareConfigurationProperties {

    @NotNull
    private List<String> roles = new ArrayList<>();

    @NotNull
    private List<String> endpoints = new ArrayList<>();

    @NotBlank
    private String defaultRole;

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public String getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
        if (this.defaultRole == null && !this.roles.isEmpty()) {
            this.defaultRole = this.roles.get(0);
        }
    }
}
