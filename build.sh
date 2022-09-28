#!/bin/bash
npm --prefix ./front-end install ./front-end
npm --prefix ./front-end run build ./front-end

mvn package -f back-end/pom.xml -DskipTests

docker build --file=back-end/Dockerfile -t gneiss-back .
docker build --file=front-end/Dockerfile -t gneiss-front .
docker-compose -f docker-compose.yaml up