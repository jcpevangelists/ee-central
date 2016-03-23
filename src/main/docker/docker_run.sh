#!/bin/bash -e
source docker_clean.sh
cd ~/server/ && docker build -t javaee-io .

cd
docker run --restart=always -d -p 80:8080 --name=javaee-io javaee-io
# docker run -t -i -p 80:8080 --name=javaee-io javaee-io
