#!/usr/bin/env bash
docker kill www &> /dev/null || true
docker rm www &> /dev/null || true
docker images | grep "<none>" | awk '{print $3}' | xargs docker rmi &> /dev/null || true
docker build -t www .
docker run --restart=always -d -p 80:8080 -e github_atoken=$1 --name=www www
docker images
docker ps
