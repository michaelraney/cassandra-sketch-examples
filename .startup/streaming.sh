#!/bin/bash
#There are two spark jobs in this demo
#A streaming job that listens to active twitter feed
#A batch job that aggregates inital sketch and creates a new perspectives

if [ `hostname` == 'node0' ]
then
  echo "Building Steaming Job... output to streaming-job-build.log"
  #Build Twitter Streaming Project
  mvn package -f dse-sketching-demo/dse-sketching-streaming-job/pom.xml > streaming-job-build.log

  #kickoff streaming job
  #moved to demo application node0:8081/config based to make use of different user credentials
  #nohup dse spark-submit --class com.se.stream.TwitterStreamSketchDemo --total-executor-cores 2 --executor-memory 1g /tmp/datastax-sketch-examples/dse-sketching-demo/dse-sketching-streaming-job/target/dse-sketching-streaming-job-jar-with-dependencies.jar > streaming.log &

  #write out current crontab for batch
  crontab -l > mycron

  #echo new cron into cron file
  echo "*/3 * * * * dse spark-submit --class com.se.rollup.DataSketchingRollup --total-executor-cores 2 --executor-memory 1g /tmp/datastax-sketch-examples/dse-sketching-demo/dse-sketching-streaming-job/target/dse-sketching-streaming-job-jar-with-dependencies.jar" >> mycron

  #install new cron file
  crontab mycron
fi
