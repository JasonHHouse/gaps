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
    populateCookieValues();
}

function populateCookieValues() {
    const address = Cookies.get('address');
    const port = Cookies.get('port');
    const plexToken = Cookies.get('plex_token');

    if (address) {
        $('#address').val(address);
    }

    if (port) {
        $('#port').val(port);
    }

    if (plexToken) {
        $('#plex_token').val(plexToken);
    }
}

function back() {
    location.assign("index.html");
}

function next() {
    Cookies.set('address', $("#address").val());
    Cookies.set('port', $("#port").val());
    Cookies.set('plex_token', $("#plex_token").val());

    location.assign("plexLibraries.html");
}
