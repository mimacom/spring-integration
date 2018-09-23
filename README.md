[![Build Status](https://travis-ci.org/mimacom/spring-integration.svg?branch=master)](https://travis-ci.org/mimacom/spring-integration)

# spring-integration
Extensions for spring-integration

## spring-integration-zookeeper-starter
A spring-boot auto-configuration starter module to configure a `CuratorFramework` 
(using the `spring-integration-zookeeper` module)

> Info: There is also the [`spring-cloud-zookeeper-core`](https://github.com/spring-cloud/spring-cloud-zookeeper/tree/master/spring-cloud-zookeeper-core) 
as a sub-module of [`spring-cloud-zookeeper`](https://github.com/spring-cloud/spring-cloud-zookeeper) 
that also auto-configures a `CuratorFramework`

## [spring-integration-leader-starter](https://github.com/mimacom/spring-integration/wiki/spring-integration-leader-starter)
A spring-boot auto-configuration starter module that allows an spring-integration endpoint to become leader aware.
Useful when multiple service-instances are running and a leader has to be elected to take care of certain tasks such as:
 * Polling against a Channel/Source
 * Running Scheduled Jobs
