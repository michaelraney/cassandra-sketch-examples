#!/bin/bash

# Download Zeppelin from Google Drive
# Zeppelin distribution by doanduyhai http://www.doanduyhai.com/blog/?p=2325

# If we are on node0 install Zeppelin, copy Notebooks and start it up.
if [ `hostname` == 'node0' ]
then
  curl -O 'https://drive.google.com/file/d/1ANt89ch9SfXfql96dXzmvRFP1rTwq3cn/view?usp=sharing' -L
  tar -zxvf zeppelin-0.7.1.tar
  cp -r /notebook/. zeppelin-0.7.1/notebook/
  zeppelin-0.7.1/bin/zeppelin-daemon.sh start
fi
