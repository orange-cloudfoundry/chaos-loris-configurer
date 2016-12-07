# chaos-loris-configurer

[![CircleCI](https://circleci.com/gh/orange-cloudfoundry/chaos-loris-configurer.svg?style=svg)](https://circleci.com/gh/orange-cloudfoundry/chaos-loris-configurer)

ease configuration of chaos-loris

## Docs

 - [Chaos-loris API details](http://strepsirrhini-army.github.io/chaos-loris/?http#overview)

## Current Status - (WIP) 
 - [x] support proxy to Chaos-Loris
 - [x] leverage spring-boot yml config
    - [x] use a yaml DSL to describe Chaos-Loris configuration
    - [ ] use cf-java-client to list application using name instead of app guid 
 - [x] create REST clients to interact with Chaos Loris 
    - [X] applications
    - [X] schedules
    - [X] chaoses
    - [ ] events
 - [ ] offers configuration service
    - [ ] reset Chaos Loris configuration
    - [ ] load a configuration
 - [ ] add an configuration exporter to backup manually updated chaos 
 - [ ] use spring-cloud-config to load app config
     - [ ] leverage refresh beans
 - [ ] enable basic auth security     
 - [ ] Clean up
    - [ ] remove Feign usage
    - [ ] remove Traverson usage
 - [ ] Polish
     - [ ] REST client
 - [ ] Test
    - [ ] mock chaos-loris 
    