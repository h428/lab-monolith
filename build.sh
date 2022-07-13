#!/bin/bash
mvn clean package -Dmaven.test.skip=true
java -jar ./lab-system/target/lab-system-1.0-SNAPSHOT.jar

