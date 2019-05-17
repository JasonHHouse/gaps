#!/bin/bash
#
# avoid dumping keys where possible
set +x

fail() {
  echo -e "Fatal: $1"
  exit 1
}
#
#if [[ -z $DB_API_KEY ]]; then
#  fail "Need to specify DB_API_KEY as environment variable\nRefer to README.md"
#fi
#
##Must be plex or folder searching
#if [[ ( -z $SEARCH_FROM_FOLDER || $SEARCH_FROM_FOLDER = false ) && ( -z $SEARCH_FROM_PLEX || $SEARCH_FROM_PLEX = false ) ]]; then
#    fail "One or both of SEARCH_FROM_FOLDER or SEARCH_FROM_PlEX must be true.\nRefer to README.md"
#fi
#
##Global to all
#PREFIX='- '
#NEW_LINE=$'\n      '
#REGEX='\,'
#
##Folder
#ALL_FOLDERS=""
#ALL_MOVIE_FORMATS=""
#DEFAULT_RECURSIVE_FLAG=false
#DEFAULT_YEAR_REGEX="\(([1-2][0-9][0-9][0-9])\)"
#if [[ test -z $SEARCH_FROM_FOLDER && $SEARCH_FROM_FOLDER = true ]]; then
#
#    if [[ -z $RECURSIVE ]]; then
#        DEFAULT_RECURSIVE_FLAG=$RECURSIVE
#    fi
#
#    if [[ -x $YEAR_REGEX ]]; then
#        DEFAULT_YEAR_REGEX=$YEAR_REGEX
#    fi
#
#    if [[ $FOLDERS =~ $REGEX ]]; then
#        IFS=$REGEX read -r -a array <<< "$FOLDERS"
#        for element in "${array[@]}"
#        do
#            ALL_FOLDERS=$ALL_FOLDERS$PREFIX$element$NEW_LINE
#	    done
#    else
#        ALL_FOLDERS=$PREFIX$FOLDERS
#    fi
#
#    if [[ $MOVIE_FORMATS =~ $REGEX ]]; then
#        IFS=$REGEX read -r -a array <<< "$MOVIE_FORMATS"
#        for element in "${array[@]}"
#        do
#            ALL_MOVIE_FORMATS=$ALL_MOVIE_FORMATS$PREFIX$element$NEW_LINE
#	    done
#    else
#        ALL_MOVIE_FORMATS=$PREFIX$MOVIE_FORMATS
#    fi
#
#else
#    echo "Not searching from Folder."
#fi
#
#
##Plex
#ALL_PLEX_ADDRESSES=""
#DEFAULT_CONNECT_TIMEOUT=180
#DEFAULT_WRITE_TIMEOUT=180
#DEFAULT_READ_TIMEOUT=180
#if [[ test -z $SEARCH_FROM_PLEX && $SEARCH_FROM_PLEX = true ]]; then
#    if [[ -z $PLEX_ADDRESS ]]; then
#        fail "Need to specify PLEX_ADDRESS as environment variable if setting SEARCH_FROM_PLEX true.\nRefer to README.md"
#    fi
#
#    if [[ $PLEX_ADDRESS =~ $REGEX ]]; then
#        IFS=$REGEX read -r -a array <<< "$PLEX_ADDRESS"
#        for element in "${array[@]}"
#        do
#            ALL_PLEX_ADDRESSES=$ALL_PLEX_ADDRESSES$PREFIX$element$NEW_LINE
#	    done
#    else
#        ALL_PLEX_ADDRESSES=$PREFIX$PLEX_ADDRESS
#    fi
#
#    if [[ -z ${CONNECT_TIMEOUT} ]]; then
#        echo "No connect timeout found. Defaulting to 180 seconds"
#        DEFAULT_CONNECT_TIMEOUT=180
#    else
#        DEFAULT_CONNECT_TIMEOUT=${CONNECT_TIMEOUT}
#    fi
#
#    if [[ -z ${WRITE_TIMEOUT} ]]; then
#        echo "No connect timeout found. Defaulting to 180 seconds"
#        DEFAULT_WRITE_TIMEOUT=180
#    else
#        DEFAULT_WRITE_TIMEOUT=${WRITE_TIMEOUT}
#    fi
#
#    if [[ -z ${READ_TIMEOUT} ]]; then
#        echo "No connect timeout found. Defaulting to 180 seconds"
#        READ_TIMEOUT=180
#    else
#        DEFAULT_READ_TIMEOUT=${READ_TIMEOUT}
#    fi
#else
#    echo "Not searching from Plex."
#fi
#
#if [[ -z $WRITE_TO_FILE ]]; then
#  fail "Need to specify WRITE_TO_FILE as environment variable\nRefer to README.md"
#fi
#
#cat > /usr/src/app/src/main/resources/application.yaml <<EOF
#gaps:
#    movieDbApiKey: ${DB_API_KEY}
#    writeToFile: ${WRITE_TO_FILE}
#    folder:
#        searchFromFolder: ${SEARCH_FROM_FOLDER}
#        folders:
#            ${ALL_FOLDERS}
#        recursive: ${DEFAULT_RECURSIVE_FLAG}
#        movieFormats:
#            ${ALL_MOVIE_FORMATS}
#        yearRegex: ${DEFAULT_YEAR_REGEX}
#    plex:
#        searchFromPlex: true
#        movieUrls:
#            ${ALL_PLEX_ADDRESSES}
#        connectTimeout: ${DEFAULT_CONNECT_TIMEOUT}
#        writeTimeout: ${DEFAULT_WRITE_TIMEOUT}
#        readTimeout: ${DEFAULT_READ_TIMEOUT}
#    movieDbListId: $TMDBLISTID
#
#logging:
#  level:
#    root: INFO
#EOF

exec mvn spring-boot:run
