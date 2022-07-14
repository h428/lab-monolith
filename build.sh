#!/bin/bash
mvn clean package -Dmaven.test.skip=true
kill -9 $(lsof -i tcp:8080 -t)
java -jar ./lab-system/target/lab-system-1.0-SNAPSHOT.jar
cd ./lab-system/target/
nohup \
  java -Xms512m -Xmx512m \
  -Xloggc:/lab/logs/lab-system-8080/gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=20M -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause \
  -jar lab-system-1.0-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  > /dev/null 2>&1 &
cd ../../
