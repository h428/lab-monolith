#!/bin/bash
kill -9 $(lsof -i tcp:8080 -t)
# java -jar ./lab-system/target/lab-system-1.0-SNAPSHOT.jar --spring.profiles.active=prod
cd ./lab-system/target/
nohup \
  java -Xms512m -Xmx512m \
  -jar lab-system-1.0-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  > /lab/logs/lab-system-8080.log 2>&1 &
cd ../../
