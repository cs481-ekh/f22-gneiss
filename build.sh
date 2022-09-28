#!/bin/bash
echo "front-end: npm install"
npm --prefix ./front-end install ./front-end || ( echo "FAILED -- front-end: npm install"; exit 1; )
echo "front-end: npm build"
npm --prefix ./front-end run build ./front-end || ( echo "FAILED -- front-end: npm build"; exit 1; )

echo "back-end: mvn package -DskipTests"
mvn package -f back-end/pom.xml -DskipTests || ( echo "FAILED -- back-end: mvn package -DskipTests"; exit 1; )

echo "docker build back-end"
docker build --file=back-end/Dockerfile -t gneiss-back . || ( echo "FAILED -- docker build back-end"; exit 1; )
echo "docker build front-end"
docker build --file=front-end/Dockerfile -t gneiss-front . || ( echo "FAILED -- docker build front-end"; exit 1; )
echo "docker-compose"
docker-compose -f docker-compose.yaml up || ( echo "FAILED -- docker-compose"; exit 1; )