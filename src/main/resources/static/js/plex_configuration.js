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

function onStart() {
    $("#back").click(function () {
        location.assign("index.html");
    });

    $("#next").click(function () {
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
        M.toast({html: "Plex IP address must not be empty"});
        return false;
    }

    if (!$("#port").val()) {
        M.toast({html: "Plex port must not be empty"});
        return false;
    }

    if (!$("#plex_token").val()) {
        M.toast({html: "Plex Token must not be empty"});
        return false;
    }

    return true;
}