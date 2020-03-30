#!/bin/bash
read -n 1 -s -r -p "Are the ports set correctly?"
VERSION=0.2.2
DOCKER_SSL_LATEST="housewrecker/gaps:latest"
DOCKER_NO_SSL_LATEST="housewrecker/gaps:latest-no-ssl"
JAR_VERSION="GapsWeb/target/GapsWeb-$VERSION.jar"
ZIP_VERSION="Gaps-$VERSION.zip"
mvn clean install
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