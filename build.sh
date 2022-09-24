#!/bin/bash
docker build --file=back-end/Dockerfile -t gneiss-back .
docker build --file=front-end/Dockerfile -t gneiss-front .
docker-compose -f docker-compose.yaml up
exit 127