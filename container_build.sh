#!/bin/bash
mkdir -p /opt/copy/
cp -R /opt/project/* /opt/copy/
cd /opt/copy/static && npm install
cd /opt/copy/static && gulp build
cd /opt/copy/ && mvn clean install tomee:run
