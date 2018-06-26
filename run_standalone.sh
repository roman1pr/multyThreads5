#!/usr/bin/env bash

spark-submit \
    --class Spark \
    --master spark://hadoop-master:7077 \
    --driver-memory 4g \
    --executor-memory 2g \
    --executor-cores 1 \
    app.jar hadoop-master /nasa/Aug
