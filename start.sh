#!/bin/sh
echo "Enable SSL: $ENABLE_SSL"
echo "Enable Login: $ENABLE_LOGIN"
echo "Base URL: $BASE_URL"
echo "Jar File: $JAR_FILE"
if [ $ENABLE_SSL == true ]; then
  if [ $ENABLE_LOGIN == true ]; then
    java -Xms256m -Xmx2048m -jar -Dspring.profiles.active=ssl -Dserver.servlet.context-path=$BASE_URL $JAR_FILE
  else
    java -Xms256m -Xmx2048m -jar -Dspring.profiles.active=ssl-no-login -Dserver.servlet.context-path=$BASE_URL $JAR_FILE
  fi
else
    if [ $ENABLE_LOGIN == true ]; then
      java -Xms256m -Xmx2048m -jar -Dspring.profiles.active=no-ssl -Dserver.servlet.context-path=$BASE_URL $JAR_FILE
    else
      java -Xms256m -Xmx2048m -jar -Dspring.profiles.active=no-ssl-no-login -Dserver.servlet.context-path=$BASE_URL $JAR_FILE
    fi
fi