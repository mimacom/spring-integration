package com.mimacom.spring.integration.leader.providers;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring-integration.leader")
public class LeaderConfigurationProperties {

    @NotBlank
    private String role = "cluster";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
