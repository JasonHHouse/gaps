#!/bin/bash
read -n 1 -s -r -p "Are the ports set correctly?"
VERSION="0.2.1-alpha"
DOCKER_SSL_VERSION="housewrecker/gaps:v$VERSION"
DOCKER_NO_SSL_VERSION="housewrecker/gaps:v$VERSION-no-ssl"
JAR_VERSION="GapsWeb/target/GapsWeb-$VERSION.jar"
ZIP_VERSION="Gaps-$VERSION.zip"
mvn clean install
docker build -f Dockerfile.ssl -t $DOCKER_SSL_VERSION .
docker push $DOCKER_SSL_VERSION
docker build -f Dockerfile.no-ssl -t $DOCKER_NO_SSL_VERSION .
docker push $DOCKER_NO_SSL_VERSION
mkdir -p GapsOnWindows
rm $ZIP_VERSION
rm GapsOnWindows/*.jar
cp $JAR_VERSION GapsOnWindows/
zip -r $ZIP_VERSION GapsOnWindows/