FROM java:openjdk-8

RUN apt-get update && apt-get install -y openssh-server

WORKDIR /root

# install hadoop 2.7.5
RUN wget http://mirror.linux-ia64.org/apache/hadoop/common/hadoop-2.7.5/hadoop-2.7.5.tar.gz && \
    tar -xzf hadoop-2.7.5.tar.gz && \
    mv hadoop-2.7.5 /usr/local/hadoop && \
    rm hadoop-2.7.5.tar.gz

# install spark 2.2.0
RUN wget http://mirror.linux-ia64.org/apache/spark/spark-2.2.0/spark-2.2.0-bin-hadoop2.7.tgz && \
    tar -xzf spark-2.2.0-bin-hadoop2.7.tgz && \
    mv spark-2.2.0-bin-hadoop2.7 /usr/local/spark && \
    rm spark-2.2.0-bin-hadoop2.7.tgz

RUN wget ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz && \
    gzip -d NASA_access_log_Jul95.gz && \
    wget ftp://ita.ee.lbl.gov/traces/NASA_access_log_Aug95.gz && \
    gzip -d NASA_access_log_Aug95.gz

# set environment variable
ENV HADOOP_HOME=/usr/local/hadoop
ENV HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop

ENV SPARK_HOME=/usr/local/spark

ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin:/usr/local/spark/bin:/usr/local/spark/sbin:/root

# ssh without key
RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

RUN mkdir -p ~/hdfs/namenode && \
    mkdir -p ~/hdfs/datanode

COPY conf/* /tmp/

RUN mv /tmp/ssh_config ~/.ssh/config && \
    mv /tmp/hadoop-env.sh $HADOOP_CONF_DIR/ && \
    mv /tmp/hdfs-site.xml $HADOOP_CONF_DIR/ && \
    mv /tmp/core-site.xml $HADOOP_CONF_DIR/ && \
    mv /tmp/yarn-site.xml $HADOOP_CONF_DIR/ && \
    cp /tmp/slaves $HADOOP_CONF_DIR/ && \
    mv /tmp/slaves $SPARK_HOME/conf/ && \
    mv /tmp/spark-defaults.conf $SPARK_HOME/conf/ && \
    mv /tmp/spark-env.sh $SPARK_HOME/conf/

RUN chmod +x $HADOOP_CONF_DIR/hadoop-env.sh && \
    chmod +x $SPARK_HOME/conf/spark-env.sh && \
    chmod +x $HADOOP_HOME/sbin/start-dfs.sh && \
    chmod +x $HADOOP_HOME/sbin/start-yarn.sh

# format namenode
RUN hdfs namenode -format

CMD [ "/bin/bash", "-c", "service ssh start; tail -f /dev/null"]