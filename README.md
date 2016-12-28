# chaos-loris-configurer

[![CircleCI](https://circleci.com/gh/orange-cloudfoundry/chaos-loris-configurer.svg?style=svg)](https://circleci.com/gh/orange-cloudfoundry/chaos-loris-configurer)

ease configuration of chaos-loris

## Docs

 - [Chaos-loris API details](http://strepsirrhini-army.github.io/chaos-loris/?http#overview)

## Current Status - (WIP) 
 - [X] support proxy to Chaos-Loris
 - [X] leverage spring-boot yml config
    - [X] use a yaml DSL to describe Chaos-Loris configuration
    - [X] use cf-java-client to list application using name instead of app guid 
 - [X] create REST clients to interact with Chaos Loris 
    - [X] applications
    - [X] schedules
    - [X] chaoses
    - [ ] events
 - [X] offers configuration service
    - [X] reset Chaos Loris configuration
    - [X] load a default configuration
    - [ ] load any configuration
 - [ ] add an configuration exporter to backup manually updated chaos 
 - [ ] use spring-cloud-config to load app config
     - [ ] leverage refresh beans
 - [ ] enable basic auth security     
 - [ ] Clean up
    - [ ] remove Feign usage (does not support HAL)
    - [ ] remove Traverson usage
 - [ ] Polish
     - [ ] REST client
 - [ ] Ops ready
    - [ ] add metrics (creation/deletion)
    - [ ] enhance log message
    - [ ] configuration validation
 - [ ] Test
    - [ ] mock chaos-loris
    - [ ] acceptance test
    - [ ] integration test (requires a CF...)
 - [ ] docs
    - [ ] user guide
        - [ ] deployment scripts
        - [ ] use [Spring restdocs](https://projects.spring.io/spring-restdocs/) to document out API
    - [ ] design 
    
    
##

![Overview](http://g.gravizo.com/source?https%3A%2F%2Fgithub.com%2Forange-cloudfoundry%2Fchaos-loris-configurer%2Fraw%2Fdocs%2Foverview.puml)
