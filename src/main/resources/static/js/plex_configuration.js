"use strict"

function onStart() {
    $('#back').click(function () {
        location.assign("index.html");
    });

    $('#next').click(function () {
        if (validateInput()) {
            let obj;
            try {
                obj = JSON.parse(document.cookie);
            } catch (e) {
                //report error here, missing movie db api key
                obj = {};
            }

            obj.address = $('#address').val();
            obj.port = $('#port').val();
            obj.plex_token = $('#plex_token').val();

            document.cookie = JSON.stringify(obj);

            location.assign("plex_libraries.html");
        }
    });

    populateCookieValues();

    M.AutoInit();
    M.updateTextFields();
}

function populateCookieValues() {
    if (document.cookie) {
        let obj = JSON.parse(document.cookie);

        if (obj.address) {
            $('#address').val(obj.address);
        }

        if (obj.port) {
            $('#port').val(obj.port);
        }

        if (obj.plex_token) {
            $('#plex_token').val(obj.plex_token);
        }
    }
}

function validateInput() {
    if (!$('#address').val()) {
        M.toast({ html: 'Plex IP address must not be empty' });
        return false;
    }

    if (!$('#port').val()) {
        M.toast({ html: 'Plex port must not be empty' });
        return false;
    }

    if (!$('#plex_token').val()) {
        M.toast({ html: 'Plex Token must not be empty' });
        return false;
    }

    return true;
}