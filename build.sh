#!/bin/bash
VERSION=0.4.0-SNAPSHOT
DOCKER_SSL_LATEST="housewrecker/gaps:latest"
DOCKER_NO_SSL_LATEST="housewrecker/gaps:latest-no-ssl"
JAR_VERSION="GapsWeb/target/GapsWeb-$VERSION.jar"
ZIP_VERSION="Gaps-$VERSION.zip"
npm run minify-input-css
npm run uglifyjs-index-js
npm run uglifyjs-configuration-js
npm run uglifyjs-libraries-js
npm run uglifyjs-recommended-js
npm run uglifyjs-common-js
npm run uglifyjs-payload-js
npm run uglifyjs-mislabeled-js
mvn clean install spotbugs:check
cypress run
docker build -f Dockerfile.ssl -t $DOCKER_SSL_LATEST .
docker push $DOCKER_SSL_LATEST
docker build -f Dockerfile.no-ssl -t $DOCKER_NO_SSL_LATEST .
docker push $DOCKER_NO_SSL_LATEST
mkdir -p GapsOnWindows
rm $ZIP_VERSION
rm GapsOnWindows/*.jar
cp $JAR_VERSION GapsOnWindows/
zip -r $ZIP_VERSION GapsOnWindows/