#!/bin/bash
VERSION=0.4.7
DOCKER_LATEST="housewrecker/gaps:latest"
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
mvn clean install deploy
docker build -f Dockerfile -t $DOCKER_LATEST .
cypress run
docker buildx build --platform linux/ppc64le,linux/s390x,linux/amd64 -t housewrecker/gaps:latest -f Dockerfile --push .
docker buildx build --platform linux/amd64,linux/arm64,linux/arm/v7 -t housewrecker/gaps:arm-latest -f Dockerfile.arm64 --push .
mkdir -p GapsOnWindows
rm $ZIP_VERSION
rm GapsOnWindows/*.jar
cp $JAR_VERSION GapsOnWindows/
zip -r $ZIP_VERSION GapsOnWindows/