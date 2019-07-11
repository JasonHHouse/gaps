"use strict"

function onStart() {
    $('#plex').click(function () {
        if (validateInput()) {
            let obj;
            try {
                obj = JSON.parse(document.cookie);
            } catch (e) {
                obj = {};
            }

            /*if (!obj) {
                obj = {
                    'movie_db_api_key': $('#movie_db_api_key').val()
                };
            } else {*/
            obj.movie_db_api_key = $('#movie_db_api_key').val();
            /*}*/
            document.cookie = JSON.stringify(obj);

            location.replace("plex_configuration.html");
        }
    });

    populateCookieValues();

    M.AutoInit();
    M.updateTextFields();
}

function populateCookieValues() {
    if (document.cookie) {
        try {
            let obj = JSON.parse(document.cookie);

            if (obj.movie_db_api_key) {
                $('#movie_db_api_key').val(obj.movie_db_api_key);
            }
        } catch (e) {
        //Nothing, no cookie
        }
    }
}

function validateInput() {
    if (!$('#movie_db_api_key').val()) {
        M.toast({ html: 'The MovieDB api key must not be empty' });
        return false;
    }

    return true;
}