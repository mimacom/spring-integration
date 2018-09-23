[![Build Status](https://travis-ci.org/mimacom/spring-integration.svg?branch=master)](https://travis-ci.org/mimacom/spring-integration)

# spring-integration
Extensions for spring-integration

## spring-integration-zookeeper-starter
A spring-boot auto-configuration starter module to configure a CuratorFramework using spring-integration-zookeeper

Info: There is also the https://github.com/spring-cloud/spring-cloud-zookeeper that takes care of that

## spring-integration-leader-starter
A spring-boot auto-configuration starter module that allows an AbstractEndpoint to become "Leader" aware.
Useful when multiple service-instances are running and a leader has to be elected to take core of certain tasks such as:
 * Polling against a Channel/Source
 * Running Scheduled Jobs
