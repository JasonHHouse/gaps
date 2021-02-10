#!/bin/bash
echo "Enable SSL: $ENABLE_SSL"
echo "Enable Login: $ENABLE_LOGIN"
echo "Base URL: $BASE_URL"
echo "Jar File: $JAR_FILE"
if [ $ENABLE_SSL == true ]; then
  if [ $ENABLE_LOGIN == true ]; then
    java -jar -Dspring.profiles.active=ssl -Dinfo.app.baseUrl=$BASE_URL $JAR_FILE
  else
    java -jar -Dspring.profiles.active=ssl-no-login -Dinfo.app.baseUrl=$BASE_URL $JAR_FILE
  fi
else
    if [ $ENABLE_LOGIN == true ]; then
      java -jar -Dspring.profiles.active=no-ssl -Dinfo.app.baseUrl=$BASE_URL $JAR_FILE
    else
      java -jar -Dspring.profiles.active=no-ssl-no-login -Dinfo.app.baseUrl=$BASE_URL $JAR_FILE
    fi
fi