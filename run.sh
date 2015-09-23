#!/usr/bin/env bash
docker kill tomitribe-io || true
docker rm tomitribe-io || true
cd docker && docker build -t tomitribe-io .
cd ..
docker run --rm -t -i -e github_atoken=$github_atoken -v $(pwd):/opt/project:ro -p 80:8080 tomitribe-io
