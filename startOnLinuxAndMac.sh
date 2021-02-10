#!/bin/bash
enable_ssl=false
enable_login=false
base_url=""

while getopts s:l:u: flag
do
    case "${flag}" in
        s) enable_ssl=${OPTARG};;
        l) enable_login=${OPTARG};;
        u) base_url=${OPTARG};;
        *) echo "usage: $0 [-s] [-l] [-u]" >&2
          exit 1 ;;
    esac
done

if [ "$enable_ssl" == true ]; then
  if [ "$enable_login" == true ]; then
    echo "Running with SSL and Login"
    java -jar -Dspring.profiles.active=ssl -Dinfo.app.baseUrl=$base_url gaps.jar
  else
    echo "Running with SSL and without Login"
    java -jar -Dspring.profiles.active=ssl-no-login -Dinfo.app.baseUrl=$base_url gaps.jar
  fi
else
    if [ "$enable_login" == true ]; then
      echo "Running without SSL and with Login"
      java -jar -Dspring.profiles.active=no-ssl -Dinfo.app.baseUrl=$base_url gaps.jar
    else
      echo "Running without SSL and without Login"
      java -jar -Dspring.profiles.active=no-ssl-no-login -Dinfo.app.baseUrl=$base_url gaps.jar
    fi
fi