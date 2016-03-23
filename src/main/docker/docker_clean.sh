#!/bin/bash -e
source docker_kill.sh
docker images | grep "<none>" | awk '{print $3}' | xargs docker rmi || true
docker images
