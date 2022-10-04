#!/bin/bash
echo "front-end: npm test"
CI=true npm --prefix ./front-end test -- --coverage ./front-end || ( echo "FAILED -- front-end: npm test"; exit 1; )
echo "front-end: npx prettier"
npx --prefix ./front-end prettier ./front-end/src || ( echo "FAILED -- front-end: npx prettier"; exit 1; )

echo "back-end: mvn test"
mvn test -f back-end/pom.xml || ( echo "FAILED -- back-end: mvn test"; exit 1; )
echo "Set up ktlint"
( curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.44.0/ktlint && chmod a+x ktlint && sudo mv ktlint /usr/local/bin/ ) || ( echo "FAILED -- Set up ktlint"; exit 1 )
echo "back-end: ktlint"
( cd back-end/src/ && ktlint ) || ( echo "FAILED -- back-end: ktlint"; exit 1 ) 
