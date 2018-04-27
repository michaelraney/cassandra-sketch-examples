#!/bin/bash

#Create Tables and Solr Schema.
#

if [ `hostname` == 'node0' ]
then
   cqlsh node0 -f cql/setupTables.cql

   #race condition
   sleep 10s
   #run repar for tables that changed replication factor
   #modified dsefs keyspace in cql startup script
   nodetool repair -pr

   sleep 10s
fi
