[![Build Status](https://travis-ci.org/mimacom/spring-integration.svg?branch=master)](https://travis-ci.org/mimacom/spring-integration)

# spring-integration
Extensions for spring-integration

## spring-integration-zookeeper-starter
A spring-boot auto-configuration starter module to configure a `CuratorFramework` (using the `spring-integration-zookeeper` module)

_Info: There is also the `spring-cloud-zookeeper-core` as a sub-module of `spring-cloud-zookeeper` (see: https://github.com/spring-cloud/spring-cloud-zookeeper)
that also configures a `CuratorFramework`_

## spring-integration-leader-starter
A spring-boot auto-configuration starter module that allows an AbstractEndpoint to become "Leader" aware.
Useful when multiple service-instances are running and a leader has to be elected to take core of certain tasks such as:
 * Polling against a Channel/Source
 * Running Scheduled Jobs

For more information see here: https://github.com/mimacom/spring-integration/wiki/spring-integration-leader-starter
