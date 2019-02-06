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

URL=""
PREFIX='- '
NEW_LINE=$'\n      '

REGEX='\,'
#echo $PLEXADDRESS
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
logging:
  level:
    root: INFO
EOF

cat /usr/src/app/src/main/resources/application.yaml
exec mvn spring-boot:run
