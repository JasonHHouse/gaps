"use strict"

function start() {
    $('.modal').modal();
}

var keepChecking;

function onSubmitGapsSearch() {
    keepChecking = true;
    $('#progressContainer').hide();

    var gaps = {
        movieDbApiKey: $('#movie_db_api_key').val(),
        writeToFile: true,
        movieDbListId: $('#movie_db_list_id').val(),
        searchFromPlex: true,
        connectTimeout: $('#connect_timeout').val(),
        writeTimeout: $('#write_timeout').val(),
        readTimeout: $('#read_timeout').val(),
        movieUrls: [$('#plex_movie_urls').val()]
    }

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 10000000,
        success: function (movies) {
            keepChecking = false;
            var movieHtml = "";
            movies.forEach(function(movie) {
                movieHtml += buildMovieDiv(movie);
            });

            $('#progressContainer').hide();
            $('#searchingBody').html(buildMovies(movieHtml));
            $('#searchModelTitle').text(movies.length + ' Movies missing from your collections');
        },
        error: function (err) {
            alert(err.responseText);
        }
    })

    $('#searchModal').modal('open');
    
    polling();
}

function buildMovies(html) {
    return '<ul class="collection">' + html + '</ul>';
}

function buildMovieDiv(movie) {
    return '<li class="collection-item">' + buildMovie(movie) + '</li>';
}

function buildMovie(movie) {
    return movie.name + " (" + movie.year + ") from '" + movie.collection + "'";
}

function polling() {
    if(keepChecking) {
        $.ajax({
        type: "GET",
        url: "http://localhost:8080/status",
        contentType: "application/json",
        success: function(data) {
                if(keepChecking) {
                    var obj = JSON.parse(data);
                    if(!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0) {
                        $('#searchingBody').text("Looking for movies...");
                    } else {
                        $('#progressContainer').show();
                        var percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
                        $('#searchingBody').text(obj.searchedMovieCount + ' of ' + obj.totalMovieCount + " movies searched. " + percentage + "% complete.");
                        $('#progressBar').css( "width", percentage + "%" );
                    }
                    setTimeout(polling, 2000);
                }
            }
        })
    }
}

function searchPlexChanged(checkbox) {
    if (checkbox.checked) {
        $("#plex_movie_urls").prop('disabled', false);
        $("#connect_timeout").prop('disabled', false);
        $("#write_timeout").prop('disabled', false);
        $("#read_timeout").prop('disabled', false);
    } else {
        $("#plex_movie_urls").prop('disabled', true);
        $("#connect_timeout").prop('disabled', true);
        $("#write_timeout").prop('disabled', true);
        $("#read_timeout").prop('disabled', true);
    }
}

function searchFolderChanged(checkbox) {
    if (checkbox.checked) {
        $("#folder_directory").prop('disabled', false);
        $("#folder_recursive").removeAttr("disabled");
        $("#movie_formats").prop('disabled', false);
        $("#folder_regex").prop('disabled', false);
    } else {
        $("#folder_directory").prop('disabled', true)
        $("#folder_recursive").attr("disabled", true);
        $("#movie_formats").prop('disabled', true);
        $("#folder_regex").prop('disabled', true);
    }
}
