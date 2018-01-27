#!/bin/bash

#Create Tables and Solr Schema.
#

if [ `hostname` == 'node0' ]
then
   cqlsh node0 -f .startup/cql/setupTables.cql
fi
