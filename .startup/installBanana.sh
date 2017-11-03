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
  curl -o banana-release.tar.gz 'https://drive.google.com/uc?id=1PaRm1ulbqWp5SiauB8liu83cfjTFP9FN' -L
  unzip banana-release.tar.gz
  mkdir /etc/dse/banana-release/
  cp -r banana-release/. /etc/dse/banana-release/

  #tomcat server xml
  curl -o server.xml 'https://drive.google.com/uc?id=1F-xJYOqsOYoZtu-RXSwrQh31Jm5rRVBC' -L
  cp server.xml /etc/dse/tomcat/
  service dse restart
fi
