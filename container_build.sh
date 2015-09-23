#!/bin/bash
cd /opt/project/static && npm install
cd /opt/project/static && gulp build
cd /opt/project/ && mvn clean install
chmod -R uao+rwx /opt/project/src/main/webapp/app || true
chmod -R uao+rwx /opt/project/target/ || true