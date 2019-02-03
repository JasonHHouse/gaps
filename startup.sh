#!/bin/sh -e

# avoid dumping keys where possible
set +x

fail() {
  echo -e "Fatal: $1"
  exit 1
}

if [ -z $DBAPIKEY ]; then
  fail "Need to specify DBAPIKEY as environment variable\nRefer to README.md"
fi

if [ -z $PLEXADDRESS ]; then 
  fail "Need to specify PLEXADDREESS as environment variable\nRefer to README.md"
fi

cat >/usr/src/app/src/main/resources/application.yaml <<EOF
plexMovieUrls: $PLEXADDRESS

movieDbApiKey: $DBAPIKEY

logging:
  level:
    root: INFO
EOF

exec mvn spring-boot:run
