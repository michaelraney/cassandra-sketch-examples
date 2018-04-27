#!/bin/bash

#Create Tables and Solr Schema.
#

if [ `hostname` == 'node0' ]
then

  echo "Has DSE Started?"
  if ! nc -z node0 9042; then
    echo "Waiting for DSE to start..."
    while ! nc -z node0 9042; do
       sleep 1
    done
  fi
  echo "DSE is ready."

   cqlsh node0 -f cql/setupTables.cql

   #race condition
   sleep 10s
   #run repar for tables that changed replication factor
   #modified dsefs keyspace in cql startup script
   nodetool repair -pr

   sleep 10s
fi
