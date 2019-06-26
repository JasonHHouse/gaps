"use strict"

function onStart() {
    $('#plex').click(function () {
        if (validateInput()) {
            location.replace("plex_configuration.html");

            let obj = JSON.parse(document.cookie);

            if (!obj) {
                obj = {
                    'movie_db_api_key': $('#movie_db_api_key').val()
                };
            } else {
                obj.movie_db_api_key = $('#movie_db_api_key').val();
            }
            document.cookie = JSON.stringify(obj);
        }
    });

    populateCookieValues();

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
    if (!$('#movie_db_api_key').val()) {
        M.toast({ html: 'The MovieDB api key must not be empty' });
        return false;
    }

    return true;
}

function onClickBanner() {
    location.replace("index.html");
}