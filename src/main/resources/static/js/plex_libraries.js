"use strict"

let allLibraries;

function onStart() {
    $('#back').click(function () {
        location.replace("plex_configuration.html");
    });

    $('#search').click(function () {
        $('#warningModal').modal('open');
    });

    setPreloaderVisibility(true);

    //populateCookieValues();

    getLibraries();

    M.AutoInit();
    M.updateTextFields();

    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.modal');
        var instances = M.Modal.init(elems, {
            onCloseEnd: function () {
                /*  if (validateInput()) {
                     location.replace("plex_movie_list.html");
                     updatedSelectedLibraries();
                 } */
            }
        });
    });
}

function setPreloaderVisibility(bool) {
    $("#progressBar").toggle(bool);
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
            allLibraries = data;
            setPreloaderVisibility(false);
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

function generateLibrariesCheckbox() {
    let obj = JSON.parse(document.cookie);
    let selectedLibraries = obj.libraries || [];

    let row = '';
    for (let library of allLibraries) {
        row += '<div class="row"><p class="col s5"><label><input onclick="updatedSelectedLibraries()" id="'
            + library.key
            + '"type="checkbox" class="filled-in grey-text text-lighten-4"'
            + (findIfChecked(selectedLibraries, library.key) ? ' checked="checked"' : '')
            + ' /><span>' + library.title + '</span></label></p></div>';
    }

    $('#libraryCheckboxes').html(row);
}

function findIfChecked(selectedLibraries, key) {
    for (var i = 0; i < selectedLibraries.length; i++) {
        if (selectedLibraries[i].key == key) {
            return true;
        }
    }
    return false;
}

function updatedSelectedLibraries() {
    let obj = JSON.parse(document.cookie);
    let selectedLibraries = findSelectedLibraries();

    obj.libraries = selectedLibraries;
    document.cookie = JSON.stringify(obj);
}

function findSelectedLibraries() {
    let selectedLibraries = [];
    for (let library of allLibraries) {
        if ($('#' + library.key).is(':checked')) {
            selectedLibraries.push(library);
        }
    }

    return selectedLibraries;
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
    let selectedLibraries = findSelectedLibraries();

    if (selectedLibraries === undefined || selectedLibraries.length == 0) {
        M.toast({ html: 'Must select at least one library' });
        return false;
    }

    return true;
}

function searchForMovies() {
    location.replace("plex_movie_list.html");
}