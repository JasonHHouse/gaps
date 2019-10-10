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

    $("#plex").click(function () {
        if (validateInput()) {
            Cookies.set('movie_db_api_key', $("#movie_db_api_key").val());
            location.assign("plex_configuration.html");
        }
    });

    populateCookieValues();

    M.AutoInit();

    M.updateTextFields();
}

function populateCookieValues() {
    if (document.cookie) {
        const movieDbApiKey = Cookies.get('movie_db_api_key');

        if (movieDbApiKey) {
            $("#movie_db_api_key").val(movieDbApiKey);
        }
    }
}

function validateInput() {
    if (!$("#movie_db_api_key").val()) {
        M.toast({html: "The MovieDB api key must not be empty"});
        return false;
    }

    return true;
}