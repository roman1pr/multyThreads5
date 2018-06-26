#!/usr/bin/env bash

sed -i s/hadoop-master/$HOSTNAME/ $HADOOP_CONF_DIR/core-site.xml
sed -i s/hadoop-master/$HOSTNAME/ $HADOOP_CONF_DIR/yarn-site.xml

start-dfs.sh && start-yarn.sh
start-master.sh && start-slaves.sh
hdfs dfs -mkdir /nasa
cp NASA_access_log_Aug95 Aug && cp NASA_access_log_Jul95 Jul
hdfs dfs -put Aug /nasa && hdfs dfs -put Jul /nasa
echo "hadoop cluster is running"
