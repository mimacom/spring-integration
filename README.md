# spring-integration
Extensions for spring-integration


## spring-integration-leader-starter
A spring-boot autoconfiguration starter module that allows an AbstractEndpoint to become "Leader" aware.
Usefull when multiple service-instances are running but only the leader should do certain tasks such as
 * Polling against a Channel/Source
 * Running Scheduled Jobs
