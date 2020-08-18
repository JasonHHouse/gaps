#!/bin/bash
echo "Enable SSL: $ENABLE_SSL"
echo "Enable Login: $ENABLE_LOGIN"
echo "Jar File: $JAR_FILE"
if [ $ENABLE_SSL == true ]; then
  if [ $ENABLE_LOGIN == true ]; then
    java -jar -Dspring.profiles.active=ssl $JAR_FILE
  else
    java -jar -Dspring.profiles.active=ssl-no-login $JAR_FILE
  fi
else
    if [ $ENABLE_LOGIN == true ]; then
      java -jar -Dspring.profiles.active=no-ssl $JAR_FILE
    else
      java -jar -Dspring.profiles.active=no-ssl-no-login $JAR_FILE
    fi
fi

# -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005