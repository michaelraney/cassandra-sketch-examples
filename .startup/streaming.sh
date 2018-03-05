#!/bin/bash


if [ `hostname` == 'node0' ]
then
  #Build Twitter Streaming Project
  mvn package -f dse-sketching-demo/dse-sketching-streaming-job/pom.xml

  #kickoff streaming job
  nohup dse spark-submit --class com.se.stream.TwitterStreamSketchDemo --total-executor-cores 4 --executor-memory 4g /tmp/datastax-sketch-examples/dse-sketching-demo/dse-sketching-streaming-job/target/dse-sketching-streaming-job-jar-with-dependencies.jar > streaming.log &

  #write out current crontab for batch
  crontab -l > mycron

  #echo new cron into cron file
  echo "*/3 * * * * dse spark-submit --class com.se.rollup.DataSketchingRollup --total-executor-cores 2 --executor-memory 4g /tmp/datastax-sketch-examples/dse-sketching-demo/dse-sketching-streaming-job/target/dse-sketching-streaming-job-jar-with-dependencies.jar" >> mycron

  #install new cron file
  crontab mycron
fi
