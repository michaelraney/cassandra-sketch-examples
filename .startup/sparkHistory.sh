#!/bin/bash

set -x

dse hadoop fs -mkdir /spark
dse hadoop fs -mkdir /spark/events

sudo dse spark-history-server start --properties-file /tmp/datastax-approximation-examples/dse-sketching-demo/dse-sketching-demo/history-server-config/spark-defaults.conf
