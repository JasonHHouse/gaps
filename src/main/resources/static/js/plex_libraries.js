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

    getLibraries();

    M.AutoInit();
    M.updateTextFields();
}

function getLibraries() {

    let obj = JSON.parse(document.cookie);
    let token = obj.plex_token;
    let port = obj.port;
    let address = obj.address;

    if (!token || !port || !address) {
        console.error('Could not find plex token, port, or address in cookies');
        //Return error toast here
        return;
    }

    let data = {
        token: token,
        port: port,
        address: address
    };

    $.ajax({
        type: "GET",
        url: "http://" + location.hostname + ":" + location.port + "/getPlexLibraries?" + encodeQueryData(data),
        contentType: "application/json",
        success: function (data) {
            generateLibrariesCheckbox(data);
        }
    });
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data)
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    return ret.join('&');
}

function generateLibrariesCheckbox(movies) {
    let row = '';
    for (let movie of movies) {
        row += '<div class="row"><p class="col s5"><label><input type="checkbox" class="filled-in" /><span>' + movie.title + '</span></label></p></div>';
    }

    $('#libraryCheckboxes').html(row);
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