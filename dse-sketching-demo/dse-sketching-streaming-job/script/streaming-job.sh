#!/bin/bash

#Pass Twitter Credetials to script consumerKey, consumerSecret, accessToken, accessTokenSecret
echo $* >> streaming-job.log

#kickoff streaming job
nohup dse spark-submit --class com.se.stream.TwitterStreamSketchDemo --total-executor-cores 2 --executor-memory 1g $1/dse-sketching-streaming-job-jar-with-dependencies.jar $2 $3 $4 $5 >> streaming-job.log &
