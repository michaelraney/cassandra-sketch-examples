#!/bin/bash


if [ `hostname` == 'node0' ]
then
  #Build Twitter Streaming Project
  nohup mvn clean package spring-boot:run -Dserver.port=8081 -f dse-sketching-demo/dse-sketching-demo/pom.xml > applicaion.log &

fi
