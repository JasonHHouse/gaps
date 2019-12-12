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
        location.assign("plexConfiguration.html");
    });
/*
    $("#search").click(function () {
        if (Cookies.get('dialogDontShowAgain')) {
            if (validateInput()) {
                $("#warningModal").modal("open");

            }
        } else {
            if (validateInput()) {
                updatedSelectedLibraries();
                location.assign("plexMovieList.html");
            }
        }
    });*/

}
/*
function validateInput() {
    let selectedLibraries = findSelectedLibraries();

    if (selectedLibraries === undefined || selectedLibraries.length === 0) {
        M.toast({html: "Must select at least one library"});
        return false;
    }
    return true;
}

function updatedSelectedLibraries() {
    Cookies.set('libraries', JSON.stringify(findSelectedLibraries()));
}

function findSelectedLibraries() {
    let selectedLibraries = [];
    for (let library of allLibraries) {
        if ($("#" + library.key).is(":checked")) {
            selectedLibraries.push(library);
        }
    }

    return selectedLibraries;
}*/
