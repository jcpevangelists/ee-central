#!/usr/bin/env bash
docker kill tomitribe-io || true
docker rm tomitribe-io || true
docker build -t tomitribe-io .
docker run --rm -t -i -v $(pwd):/opt/project tomitribe-io

