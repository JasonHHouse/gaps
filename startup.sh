#!/bin/bash
#
# avoid dumping keys where possible
set +x

fail() {
  echo -e "Fatal: $1"
  exit 1
}

if [[ -z $DB_API_KEY ]]; then
  fail "Need to specify DB_API_KEY as environment variable\nRefer to README.md"
fi

#Must be plex or folder searching
if [[ ( -z $SEARCH_FROM_FOLDER || $SEARCH_FROM_FOLDER = false ) && ( -z $SEARCH_FROM_PLEX || $SEARCH_FROM_PLEX = false ) ]]; then
    fail "One or both of SEARCH_FROM_FOLDER or SEARCH_FROM_PlEX must be true.\nRefer to README.md"
fi

#Folder
if [[ -z $SEARCH_FROM_FOLDER && $SEARCH_FROM_FOLDER = true ]]; then
    echo "All good."
    #Remove ^
else
    echo "Not searching from Folder."
fi


#Plex
URL=""
DEFAULT_CONNECT_TIMEOUT=180
DEFAULT_WRITE_TIMEOUT=180
DEFAULT_READ_TIMEOUT=180
if [[ -z $SEARCH_FROM_PLEX && $SEARCH_FROM_PLEX = true ]]; then
    if [[ -z $PLEX_ADDRESS ]]; then
        fail "Need to specify PLEX_ADDRESS as environment variable if setting SEARCH_FROM_PLEX true.\nRefer to README.md"
    fi

    PREFIX='- '
    NEW_LINE=$'\n      '

    REGEX='\,'
    if [[ $PLEX_ADDRESS =~ $REGEX ]]; then
        IFS=$REGEX read -r -a array <<< "$PLEX_ADDRESS"
        for element in "${array[@]}"
        do
            URL=$URL$PREFIX$element$NEW_LINE
	    done
    else
        URL=$PREFIX$PLEX_ADDRESS
    fi

    if [[ -z ${CONNECT_TIMEOUT} ]]; then
        echo "No connect timeout found. Defaulting to 180 seconds"
        DEFAULT_CONNECT_TIMEOUT=180
    else
        DEFAULT_CONNECT_TIMEOUT=${CONNECT_TIMEOUT}
    fi

    if [[ -z ${WRITE_TIMEOUT} ]]; then
        echo "No connect timeout found. Defaulting to 180 seconds"
        DEFAULT_WRITE_TIMEOUT=180
    else
        DEFAULT_WRITE_TIMEOUT=${WRITE_TIMEOUT}
    fi

    if [[ -z ${READ_TIMEOUT} ]]; then
        echo "No connect timeout found. Defaulting to 180 seconds"
        READ_TIMEOUT=180
    else
        DEFAULT_READ_TIMEOUT=${READ_TIMEOUT}
    fi
else
    echo "Not searching from Plex."
fi

if [[ -z $WRITE_TO_FILE ]]; then
  fail "Need to specify WRITE_TO_FILE as environment variable\nRefer to README.md"
fi

cat > /usr/src/app/src/main/resources/application.yaml <<EOF
gaps:
    movieDbApiKey: ${DB_API_KEY}
    writeToFile: ${WRITE_TO_FILE}
    folder:
        searchFromFolder: true
        folders:
            - /Users/jhouse/Movies
        recursive: true
        movieFormats:
            - avi
            - mkv
            - mp4
            - mov
            - webm
            - vob
            - flv
            - mts
            - m2ts
            - m4p
            - m4v
            - 3gp
            - 3g2
        yearRegex: \(([1-2][0-9][0-9][0-9])\)
    plex:
        searchFromPlex: true
        movieUrls:
            ${URL}
        connectTimeout: ${DEFAULT_CONNECT_TIMEOUT}
        writeTimeout: ${DEFAULT_WRITE_TIMEOUT}
        readTimeout: ${DEFAULT_READ_TIMEOUT}
    movieDbListId: $TMDBLISTID

logging:
  level:
    root: INFO
EOF

exec mvn spring-boot:run
