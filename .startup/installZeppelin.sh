#!/bin/bash

# Download Zeppelin from Google Drive
# Zeppelin distribution by doanduyhai http://www.doanduyhai.com/blog/?p=2325

# If we are on node0 install Zeppelin, copy Notebooks and start it up.
if [ `hostname` == 'node0' ]
then
  curl -o zeppelin-0.7.1.tar 'https://s3.amazonaws.com/dse-sketch-examples/zeppelin-0.7.1.tar.gz' -L
  tar -zxvf zeppelin-0.7.1.tar
  cp -r notebook/. zeppelin-0.7.1/notebook/
  zeppelin-0.7.1/bin/zeppelin-daemon.sh start
fi
