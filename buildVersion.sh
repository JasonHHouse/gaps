#!/bin/bash
VERSION="0.4.0-SNAPSHOT"
DOCKER_SSL_VERSION="housewrecker/gaps:v$VERSION"
DOCKER_NO_SSL_VERSION="housewrecker/gaps:v$VERSION-no-ssl"
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
docker build -f Dockerfile.no-ssl-no-login -t gaps-dev  .
docker run -p 8484:8484 --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev
cypress run
docker build -f Dockerfile.ssl -t $DOCKER_SSL_VERSION .
docker build -f Dockerfile.no-ssl -t $DOCKER_NO_SSL_VERSION .
docker push $DOCKER_SSL_VERSION
docker push $DOCKER_NO_SSL_VERSION
mkdir -p GapsOnWindows
rm $ZIP_VERSION
rm GapsOnWindows/*.jar
cp $JAR_VERSION GapsOnWindows/
zip -r $ZIP_VERSION GapsOnWindows/