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

function savePlex() {
    const address = $('#address').val();
    const finalVote = $('#finalVote').val();
    const skipTicket = $('#skipTicket').is(":checked");

    const obj = {
        'address': address,
        'port': port
    };

    $.ajax({
        type: "POST",
        url: '/savePlexConfiguration',
        data: obj,
        dataType: 'application/json'
    });
}

function saveTmdb() {
    const movieDbApiKey = $('#movieDbApiKey').val();

    const obj = {
        'movieDbApiKey': movieDbApiKey,
    };

    $.ajax({
        type: "POST",
        url: '/saveTmdbConfiguration',
        data: obj,
        dataType: 'application/json'
    });
}
