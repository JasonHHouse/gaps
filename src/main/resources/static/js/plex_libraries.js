"use strict"

let libraries;

function onStart() {
    $('#back').click(function () {
        location.replace("plex_configuration.html");
    });

    $('#next').click(function () {
        if (validateInput()) {
            location.replace("plex_movie_list.html");

            let obj = JSON.parse(document.cookie);

            let selectedLibraries = [];
            for (library of libraries) {

                if ($('#' + library).is(':checked')) {
                    selectedLibraries.push(library);
                }

            }

            obj.libraries = selectedLibraries;
            document.cookie = JSON.stringify(obj);
        }
    });

    //populateCookieValues();

    M.AutoInit();
    M.updateTextFields();
}

function populateCookieValues() {
    if (document.cookie) {
        let obj = JSON.parse(document.cookie);

        if (obj.movie_db_api_key) {
            $('#movie_db_api_key').val(obj.movie_db_api_key);
        }

    }
}

function validateInput() {
    /*if (!$('#movie_db_api_key').val()) {
        M.toast({ html: 'The MovieDB api key must not be empty' });
        return false;
    }*/

    return true;
}