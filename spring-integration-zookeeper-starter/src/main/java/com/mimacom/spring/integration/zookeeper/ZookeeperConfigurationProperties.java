package com.mimacom.spring.integration.zookeeper;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring-integration.zookeeper")
public class ZookeeperConfigurationProperties {

    @NotBlank
    private String connection;

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }
}
