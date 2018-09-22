package com.mimacom.spring.integration.leader.providers.zookeeper;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("spring-integration.leader.zookeeper")
@Validated
public class ZookeeperLeaderConfigurationProperties {

    @NotBlank
    private String path = "/spring-integration/leader/";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
