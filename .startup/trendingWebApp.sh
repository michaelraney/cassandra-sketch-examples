#!/bin/bash


if [ `hostname` == 'node0' ]
then
  #Build Twitter Streaming Project
  mvn clean package spring-boot:run -Dserver.port=8081 -f dse-sketching-demo/dse-sketching-demo/pom.xml

  nohup java -Xmx1g -Xms1g -Dserver.port=8081 -jar dse-sketching-demo/dse-sketching-demo/target/dse-sketching-demo.war > applicaion.log &

fi
