#!/bin/bash

#Create Tables and Solr Schema.
#

if [ `hostname` == 'node0' ]
   cqlsh -f setupTables.cql node0
fi
