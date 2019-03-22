#!/bin/bash

set -x

IP=$(ifconfig | awk '/inet/ { print $2 }' | egrep -v '^fe|^127|^192|^172|::' | head -1)
IP=${IP#addr:}

if [[ $HOSTNAME == "node"* ]] ; then
    #rightscale
    IP=$(grep $(hostname)_ext /etc/hosts | awk '{print $1}')
fi

if [[ "$OSTYPE" == "darwin"* ]]; then
    # Mac OSX
    IP=localhost
fi

# Swap out your file name here
curl -H "Accept-Encoding: gzip" -X POST -F 'file=@notebook/datastax-studio/trending_demo_notebook.studio-nb.tar' http://"$IP":9091/api/v1/notebooks/import &> /dev/null
