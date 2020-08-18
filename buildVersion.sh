#!/bin/bash
VERSION="0.5.1"
DOCKER_LATEST="housewrecker/gaps:v$VERSION"
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
npm run uglifyjs-alerts-manager-js
npm run uglifyjs-notification-types-js
npm run uglifyjs-telegram-notifications-js
npm run uglifyjs-slack-notifications-js
npm run uglifyjs-push-bullet-notifications-js
npm run uglifyjs-gotify-notifications-js
npm run uglifyjs-email-notifications-js
npm run uglifyjs-schedule-js
mvn clean install spotbugs:spotbugs pmd:pmd checkstyle:checkstyle sonar:sonar deploy
docker build -f Dockerfile -t $DOCKER_LATEST .
docker run -p 8484:8484 --name gaps-dev -v /home/jason/gaps:/usr/data:Z gaps-dev
#cypress run
docker push $DOCKER_LATEST
mkdir -p GapsOnWindows
rm $ZIP_VERSION
rm GapsOnWindows/*.jar
cp $JAR_VERSION GapsOnWindows/
zip -r $ZIP_VERSION GapsOnWindows/