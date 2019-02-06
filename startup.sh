#!/bin/bash
#
# avoid dumping keys where possible
set +x

fail() {
  echo -e "Fatal: $1"
  exit 1
}

if [[ -z $DBAPIKEY ]]; then
  fail "Need to specify DBAPIKEY as environment variable\nRefer to README.md"
fi

if [[ -z $PLEXADDRESS ]]; then
  fail "Need to specify PLEXADDRESS as environment variable\nRefer to README.md"
fi

if [[ -z $WRITETOFILE ]]; then
  fail "Need to specify WRITETOFILE as environment variable\nRefer to README.md"
fi

DEFAULT_CONNECT_TIMEOUT=180
if [[ -z ${CONNECT_TIMEOUT} ]]; then
    echo "No connect timeout found. Defaulting to 180 seconds"
    DEFAULT_CONNECT_TIMEOUT=180
else
    DEFAULT_CONNECT_TIMEOUT=${CONNECT_TIMEOUT}
fi

DEFAULT_WRITE_TIMEOUT=180
if [[ -z ${WRITE_TIMEOUT} ]]; then
    echo "No connect timeout found. Defaulting to 180 seconds"
    DEFAULT_WRITE_TIMEOUT=180
else
    DEFAULT_WRITE_TIMEOUT=${WRITE_TIMEOUT}
fi

DEFAULT_READ_TIMEOUT=180
if [[ -z ${READ_TIMEOUT} ]]; then
    echo "No connect timeout found. Defaulting to 180 seconds"
    READ_TIMEOUT=180
else
    DEFAULT_READ_TIMEOUT=${READ_TIMEOUT}
fi

URL=""
PREFIX='- '
NEW_LINE=$'\n      '

REGEX='\,'
if [[ $PLEXADDRESS =~ $REGEX ]]; then
    IFS=$REGEX read -r -a array <<< "$PLEXADDRESS"
    for element in "${array[@]}"
    do
        URL=$URL$PREFIX$element$NEW_LINE
	done
else
    URL=$PREFIX$PLEXADDRESS
fi

cat > /usr/src/app/src/main/resources/application.yaml <<EOF
gaps:
    movieUrls:
      ${URL}
    movieDbApiKey: ${DBAPIKEY}
    writeToFile: ${WRITETOFILE}
    plex:
        connectTimeout: ${DEFAULT_CONNECT_TIMEOUT}
        writeTimeout: ${DEFAULT_WRITE_TIMEOUT}
        readTimeout: ${DEFAULT_READ_TIMEOUT}
logging:
  level:
    root: INFO
EOF

exec mvn spring-boot:run
