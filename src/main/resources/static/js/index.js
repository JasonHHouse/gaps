"use strict"

function onStart() {
    $('#plex').click(function() {
        if(validateInput()) {
            location.replace("plex.html");
            let obj = {
                'movie_db_api_key' : $('#movie_db_api_key').val()
            };
            document.cookie = JSON.stringify(obj);
        }
    });
}

function validateInput() {
    if (!$('#movie_db_api_key').val()) {
        M.toast({ html: 'The MovieDB api key must not be empty' });
        return false;
    }

    return true;
}