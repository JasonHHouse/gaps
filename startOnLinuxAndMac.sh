#!/bin/bash
enable_ssl=false
enable_login=false
base_url=""
storage_folder=$PWD

while getopts s:l:u:f:h flag
do
    case "${flag}" in
        s) enable_ssl=${OPTARG};;
        l) enable_login=${OPTARG};;
        u) base_url=${OPTARG};;
        f) storage_folder=${OPTARG};;
        h) echo -e "Usage: $0 \n[-s] : Enable SSL [true/false]\n[-l] : Enable Login [true/false] \n[-u] : Set Base URL [string] \n[-f] : Set Storage Folder [string] \n[-h] : help " >&2
          exit 1;;
        *) echo -e "Usage: $0 \n[-s] : Enable SSL [true/false]\n[-l] : Enable Login [true/false] \n[-u] : Set Base URL [string] \n[-f] : Set Storage Folder [string] \n[-h] : help " >&2
          exit 1;;
    esac
done

if [ "$enable_ssl" == true ]; then
  if [ "$enable_login" == true ]; then
    echo "Running with SSL and Login"
    java -jar -Dspring.profiles.active=ssl -Dinfo.app.baseUrl=$base_url -Dinfo.app.storageFolder=$storage_folder gaps.jar
  else
    echo "Running with SSL and without Login"
    java -jar -Dspring.profiles.active=ssl-no-login -Dinfo.app.baseUrl=$base_url -Dinfo.app.storageFolder=$storage_folder gaps.jar
  fi
else
    if [ "$enable_login" == true ]; then
      echo "Running without SSL and with Login"
      java -jar -Dspring.profiles.active=no-ssl -Dinfo.app.baseUrl=$base_url -Dinfo.app.storageFolder=$storage_folder gaps.jar
    else
      echo "Running without SSL and without Login"
      java -jar -Dspring.profiles.active=no-ssl-no-login -Dinfo.app.baseUrl=$base_url -Dinfo.app.storageFolder=$storage_folder gaps.jar
    fi
fi