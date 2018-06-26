#!/usr/bin/env bash

hdfs dfs -getmerge /task1 task1
hdfs dfs -getmerge /task2 task2
hdfs dfs -getmerge /task3 task3
hdfs dfs -rm -r /task*