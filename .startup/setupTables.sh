#!/bin/bash

#Create Tables and Solr Schema.
#

if [ `hostname` == 'node0' ]
   ./cqlsh node0 -f setupTables.cql
fi
