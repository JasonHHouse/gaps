/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

"use strict";

let allLibraries;

function onStart() {
    $("#back").click(function () {
        location.assign("plex_configuration.html");
    });

    $("#search").click(function () {
        let obj = JSON.parse(document.cookie);

        if (!obj.dialogDontShowAgain) {
            if (validateInput()) {
                $("#warningModal").modal("open");

            }
        } else {
            if (validateInput()) {
                updatedSelectedLibraries();
                location.assign("plex_movie_list.html");
            }
        }
    });

    $("#agree").click(function () {
        if (validateInput()) {
            let obj = JSON.parse(document.cookie);
            obj.dialogDontShowAgain = $("#dialogDontShowAgain").is(":checked");
            document.cookie = JSON.stringify(obj);
            updatedSelectedLibraries();
            location.assign("plex_movie_list.html");
        }
    });

    setPreloaderVisibility(true);

    getLibraries();

    M.AutoInit();
    M.updateTextFields();

    document.addEventListener("DOMContentLoaded", function () {
        const elements = document.querySelectorAll(".modal");
        M.Modal.init(elements);
    });
}

function setPreloaderVisibility(bool) {
    $("#progressBar").toggle(bool);
}

function setSearchEnabled(bool) {
    if (bool) {
        $("#search").removeClass("disabled");
    } else {
        $("#search").addClass("disabled");
    }
}

function clearLibrariesAndErrors() {
    $("#libraryException").html("");
    $("#libraryCheckboxes").html("");
}

function getLibraries() {

    setSearchEnabled(false);
    clearLibrariesAndErrors();

    let obj = JSON.parse(document.cookie);
    let token = obj.plex_token;
    let port = obj.port;
    let address = obj.address;

    if (!token || !port || !address) {
        console.warn("Could not find plex token, port, or address in cookies");
        M.toast({html: "Could not find plex token, port, or address in cookies"});
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
            setSearchEnabled(true);
            generateLibrariesCheckbox(data);
        }, error: function () {
            setPreloaderVisibility(false);
            setSearchEnabled(false);
            setErrorMessage();
        }
    });
}

function encodeQueryData(data) {
    const ret = [];
    for (const d in data) {
        ret.push(encodeURIComponent(d) + "=" + encodeURIComponent(data[d]));
    }
    return ret.join("&");
}

function validateInput() {
    let selectedLibraries = findSelectedLibraries();

    if (selectedLibraries === undefined || selectedLibraries.length === 0) {
        M.toast({html: "Must select at least one library"});
        return false;
    }
    return true;
}

function setErrorMessage() {
    $("#libraryCheckboxes").html("<p>Something went wrong. Please make sure your connection to Plex is correct. You can navigate back to make changes and then retry connecting. Check the browser and Docker logs for more information.</p>");
}

function generateLibrariesCheckbox() {
    let obj = JSON.parse(document.cookie);
    let selectedLibraries = obj.libraries || [];

    let row = "";
    for (const library of allLibraries) {
        const ifChecked = findIfChecked(selectedLibraries, library.key) ? " checked='checked'" : "";
        row += `<div class='row'><p class='col s5'><label><input onclick='updatedSelectedLibraries()' id='${library.key}' type='checkbox' class='filled-in grey-text text-lighten-4' ${ifChecked} /><span>${library.title}</span></label></p></div>`;
    }

    $("#libraryCheckboxes").html(row);
}

function findIfChecked(selectedLibraries, key) {
    for (let i = 0; i < selectedLibraries.length; i++) {
        if (selectedLibraries[i].key === key) {
            return true;
        }
    }
    return false;
}

function updatedSelectedLibraries() {
    let obj = JSON.parse(document.cookie);
    obj.libraries = findSelectedLibraries();
    document.cookie = JSON.stringify(obj);
}

function findSelectedLibraries() {
    let selectedLibraries = [];
    for (let library of allLibraries) {
        if ($("#" + library.key).is(":checked")) {
            selectedLibraries.push(library);
        }
    }

    return selectedLibraries;
}