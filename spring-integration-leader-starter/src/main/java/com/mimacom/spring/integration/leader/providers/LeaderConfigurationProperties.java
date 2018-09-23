package com.mimacom.spring.integration.leader.providers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@ConfigurationProperties("spring-integration.leader")
public class LeaderConfigurationProperties implements EnvironmentAware {

    private static final String FALLBACK_ROLE_NAME = "spring-application";

    private String role;

    @Override
    public void setEnvironment(Environment environment) {
        this.role = environment.getProperty("spring.application.name", FALLBACK_ROLE_NAME);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
