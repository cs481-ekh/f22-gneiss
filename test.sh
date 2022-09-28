#!/bin/bash
CI=true npm --prefix ./front-end test -- --coverage ./front-end
npx --prefix ./front-end prettier ./front-end/src

mvn test -f back-end/pom.xml
curl -sSLO https://github.com/pinterest/ktlint/releases/download/0.44.0/ktlint && chmod a+x ktlint && sudo mv ktlint /usr/local/bin/
ktlint
