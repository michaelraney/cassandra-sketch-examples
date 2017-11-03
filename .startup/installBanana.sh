#!/bin/bash

#Banana is lucidworks port of Kabana. This is used to visualize the timeseries created
#by the demo.

# Download Banana from Google Drive
# Original Directions from Caroline's Post https://medium.com/@carolinerg/visualizing-cassandra-solr-data-with-banana-b54bf9dd24c

#Banana gets installed into the Tomcat hosting the Solr app.
#The node needs to be restarted so the new server.xml can take effect.

# If we are on node0 install Banana, copy content, restart dse node.
if [ `hostname` == 'node0' ]
then
  curl -o banana-release.tar.gz 'https://s3.amazonaws.com/dse-sketch-examples/banana-release.tar.gz' -L
  tar -zxvf banana-release.tar.gz
  sudo mkdir /etc/dse/banana-release/
  sudo cp -r banana-release/. /etc/dse/banana-release/

  #tomcat server xml
  curl -o server.xml 'https://s3.amazonaws.com/dse-sketch-examples/server.xml' -L
  sudo cp server.xml /etc/dse/tomcat/conf
  sudo service dse restart
fi
