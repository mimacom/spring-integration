package com.mimacom.spring.integration.leader.providers.zookeeper;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("spring-integration.leader.zookeeper")
@Validated
public class ZookeeperLeaderConfigurationProperties implements EnvironmentAware {

    private static final String DEFAULT_PATH_FORMAT = "/spring/leader/%s/";

    private static final String DEFAULT_PATH_NAME = "default";

    private boolean enabled = true;

    @NotBlank
    private String path;

    @Override
    public void setEnvironment(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name");
        if (!StringUtils.isEmpty(applicationName)) {
            this.path = String.format(DEFAULT_PATH_FORMAT, applicationName);
        } else {
            this.path = String.format(DEFAULT_PATH_FORMAT, DEFAULT_PATH_NAME);
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
