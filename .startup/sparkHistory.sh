#!/bin/bash

sleep 10s

#modified dsefs keyspace in cql startup script
nodetool repair -pr

sleep 10s

set -x

dse hadoop fs -mkdir /spark
dse hadoop fs -mkdir /spark/events

sudo nohup dse spark-history-server start --properties-file /tmp/datastax-sketch-examples/dse-sketching-demo/dse-sketching-demo/history-server-config/spark-defaults.conf > spark-history-server.log &
