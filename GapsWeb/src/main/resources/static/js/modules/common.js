/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

export function getOwnedMoviesForTable(url, movieContainer, noMovieContainer, moviesTable) {
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            if (result.code === 40) {
                movieContainer.show(100);
                noMovieContainer.css({'display': 'none'});
                moviesTable.rows.add(result.extras).draw();
            } else {
                movieContainer.css({'display': 'none'});
                noMovieContainer.show(100);
            }
        }, error: function () {
            movieContainer.css({'display': 'none'});
            noMovieContainer.show(100);
            //Show error + error
        }
    });
}

export function getRecommendedMoviesForTable(url, movieContainer, noMovieContainer, notSearchedYetContainer, moviesTable) {
    $.ajax({
        type: "GET",
        url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            if (result.code === 50) {
                movieContainer.show(100);
                noMovieContainer.css({'display': 'none'});
                notSearchedYetContainer.css({'display': 'none'});
                moviesTable.rows.add(result.extras).draw();
            } else if (result.code === 41) {
                movieContainer.css({'display': 'none'});
                notSearchedYetContainer.css({'display': 'none'});
                noMovieContainer.show(100);
            } else {
                movieContainer.css({'display': 'none'});
                noMovieContainer.css({'display': 'none'});
                notSearchedYetContainer.show(100);
            }
        }, error: function () {
            movieContainer.css({'display': 'none'});
            notSearchedYetContainer.css({'display': 'none'});
            noMovieContainer.show(100);
            //Show error + error
        }
    });
}