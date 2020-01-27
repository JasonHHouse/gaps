/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

$(document).ready(function () {
    const plexSearch = JSON.parse($('#plexSearch').val());

    moviesTable = $('#movies').DataTable({
        columns: [
            {
                data: "poster",
                render: function (data, type, row) {
                    if (type === 'display' && row.poster) {
                        const url = `http://${plexSearch.address}:${plexSearch.port}${row.poster}/?X-Plex-Token=${plexSearch.plexToken}`;
                        return `<img class="thumbnail" src="${url}" alt="poster">`;
                    }
                    return data;
                },
                className: "dt-body-center"
            },
            {data: "title"},
            {data: "year"},
            {data: "language"},
            {data: "link"}/*,
            {
                data: "find_missing",
                render: function (data, type, row) {
                    if (type === 'display') {
                        return '<input type="checkbox" class="editor-active">';
                    }
                    return data;
                },
                className: "dt-body-center"
            }*/
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