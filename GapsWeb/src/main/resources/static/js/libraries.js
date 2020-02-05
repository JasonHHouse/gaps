/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

let libraryTitle;
let plexServers;
let plexServer;
let moviesTable;
let key;

jQuery(function ($) {
    //const plexSearch = JSON.parse($('#plexSearch').val());

    Handlebars.registerHelper('json', function (context) {
        return JSON.stringify(context);
    });

    libraryTitle = $('#libraryTitle');
    plexServers = JSON.parse($('#plexServers').val());
    plexServer = JSON.parse($('#plexServer').val());
    key = $('#key').val();

    moviesTable = $('#movies').DataTable({
        "initComplete": function (settings, json) {
            $.ajax({
                type: "GET",
                url: `/libraries/${plexServer.machineIdentifier}/${key}`,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (result) {
                    //need to check result for valid output or not searched yet
                    console.log(JSON.parse(result.movies));
                    moviesTable.rows.add(JSON.parse(result.movies)).draw();
                }, error: function () {
                    //Show error
                    moviesTable.rows().invalidate().draw();
                }
            });
        },
        ordering: false,
        columns: [
            {
                data: "card",
                render: function (data, type, row) {
                    if (type === 'display') {
                        const obj = {
                            name: row.name,
                            year: row.year,
                            collection: row.collection,
                            poster_url: row.poster_url,
                            address: plexServer.address,
                            port: plexServer.port,
                            plexToken: plexServer.plexToken
                        };

                        const plexServerCard = $("#movieCard").html();
                        const theTemplate = Handlebars.compile(plexServerCard);
                        return theTemplate(obj);
                    }
                    return "";
                }
            },
            {
                data: "title",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.name) {
                        return row.name;
                    }
                    return "";
                }
            },
            {
                data: "year",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.year) {
                        return row.year;
                    }
                    return "";
                }
            },
            {
                data: "language",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.language) {
                        return row.language;
                    }
                    return "";
                }
            },
            {
                data: "collection",
                visible: false,
                render: function (data, type, row) {
                    if (type === 'display' && row.collection) {
                        return row.collection;
                    }
                    return "";
                }
            },
        ],
        select: {
            style: 'os',
            selector: 'td:not(:last-child)' // no row selection on last column
        },
        rowCallback: function (row, data) {
            // Set the checked state of the checkbox in the table
            $('input.editor-active', row).prop('checked', data.active == 1);
        }
    });

});

function switchPlexLibrary(machineIdentifier, key) {
    window.key = key;
    plexServer = plexServers[machineIdentifier];
    const plexLibrary = plexServer.plexLibraries.find(plexServer => plexServer.key === parseInt(key));
    libraryTitle.text(`${plexServer.friendlyName} - ${plexLibrary.title}`);

    moviesTable.data().clear();
    moviesTable.rows().invalidate().draw();

    $.ajax({
        type: "GET",
        url: `/libraries/${machineIdentifier}/${key}`,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            //need to check result for valid output or not searched yet
            console.log(JSON.parse(result.movies));
            moviesTable.rows.add(JSON.parse(result.movies)).draw();
        }, error: function () {
            //Show error
            moviesTable.rows().invalidate().draw();
        }
    });
}