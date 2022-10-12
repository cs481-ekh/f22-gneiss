#!/bin/bash
echo "front-end: npm install"
npm --prefix ./front-end install ./front-end || ( echo "FAILED -- front-end: npm install"; exit 1; )
echo "front-end: npm build"
npm --prefix ./front-end run build ./front-end || ( echo "FAILED -- front-end: npm build"; exit 1; )

echo "back-end: mvn package -DskipTests"
mvn package -f back-end/pom.xml -DskipTests || ( echo "FAILED -- back-end: mvn package -DskipTests"; exit 1; )

echo "docker build back-end"
docker build --file=back-end/Dockerfile -t gneiss-back . || ( echo "FAILED -- docker build back-end"; exit 1; )
