#!/usr/bin/env bash
docker kill tomitribe-io || true
docker rm tomitribe-io || true
docker build -t tomitribe-io .
docker run --rm -t -i -e github_atoken=$github_atoken -v $(pwd):/opt/project:ro -p 8080:8080 tomitribe-io

