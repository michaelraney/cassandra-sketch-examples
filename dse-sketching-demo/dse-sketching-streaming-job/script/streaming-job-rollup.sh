#!/bin/bash

#Pass jar location to script
echo $*

#kickoff rollup job
dse spark-submit --class com.se.rollup.DataSketchingRollup --total-executor-cores 2 --executor-memory 1g $1/dse-sketching-streaming-job-jar-with-dependencies.jar
