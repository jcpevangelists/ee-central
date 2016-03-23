#!/bin/bash -e
docker kill javaee-io &> /dev/null || true
docker rm javaee-io &> /dev/null || true
