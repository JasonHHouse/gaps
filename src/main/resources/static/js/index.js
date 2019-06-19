"use strict"

let keepChecking;
let stompClient;
let modal;

document.addEventListener('DOMContentLoaded', function () {
    M.AutoInit();

    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems, {
        onCloseEnd: function () {
            $.ajax({
                type: "PUT",
                url: "http://" + $('#address').val() + ":" + $('#port').val() + "/cancelSearch",
                contentType: "application/json",
                timeout: 0
            });
        }
    });

    var modal = M.Modal.getInstance($('#searchModal'));
});

function onSubmitGapsSearch() {
    if (validateInput()) {
        search();
    }
}

function validateInput() {
    if (!$('#movie_db_api_key').val()) {
        M.toast({ html: 'Movie DB Api Key must not be empty' });
        return false;
    }

    if (!$('#plex_movie_urls').val()) {
        M.toast({ html: 'Plex Movie URLs must not be empty' });
        return false;
    }

    if (!$('#connect_timeout').val()) {
        M.toast({ html: 'Connection timeout must not be empty' });
        return false;
    }

    if (!$('#write_timeout').val()) {
        M.toast({ html: 'Connection timeout must not be empty' });
        return false;
    }

    if (!$('#read_timeout').val()) {
        M.toast({ html: 'Connection timeout must not be empty' });
        return false;
    }

    return true;
}

function search() {
    connect();

    keepChecking = true;
    let searchModelTitle = $('#searchModelTitle');
    let progressContainer = $('#progressContainer');
    let searchingBody = $('#searchingBody');
    let modelButton = $('#modelButton');

    progressContainer.hide();
    modelButton.text('cancel');
    searchModelTitle.text("Searching");
    searchingBody.empty();

    const gaps = {
        movieDbApiKey: $('#movie_db_api_key').val(),
        writeToFile: true,
        movieDbListId: $('#movie_db_list_id').val(),
        searchFromPlex: true,
        connectTimeout: $('#connect_timeout').val(),
        writeTimeout: $('#write_timeout').val(),
        readTimeout: $('#read_timeout').val(),
        movieUrls: $('#plex_movie_urls').val().split("\n")
    }

    $.ajax({
        type: "POST",
        url: "http://" + $('#address').val() + ":" + $('#port').val() + "/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function (movies) {
            keepChecking = false;
            let movieHtml = "";
            movies.forEach(function (movie) {
                movieHtml += buildMovieDiv(movie);
            });

            progressContainer.hide();
            searchingBody.html(buildMovies(movieHtml));
            searchModelTitle.text(movies.length + ' movies to add to complete your collections');
            modelButton.text('close');

            disconnect();
        },
        error: function (err) {
            let message = "Unknown error. Check docker Gaps log file.";
            if (err) {
                message = JSON.parse(err.responseText).message;
            }

            progressContainer.hide();
            searchingBody.html(message);
            searchModelTitle.text("An error occurred...");
            modelButton.text('close');

            keepChecking = false;

            disconnect();
        }
    });

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
    if (keepChecking) {
        $.ajax({
            type: "GET",
            url: "http://" + $('#address').val() + ":" + $('#port').val() + "/status",
            contentType: "application/json",
            success: function (data) {

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

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/searchStatus', function (status) {
            showSearchStatus(JSON.parse(status.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function showSearchStatus(obj) {
    if (keepChecking) {
        if (!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0) {
            $('#searchingBody').text("Searching for movies...");
        } else {
            $('#progressContainer').show();
            var percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
            $('#searchingBody').text(obj.searchedMovieCount + ' of ' + obj.totalMovieCount + " movies searched. " + percentage + "% complete.");
            $('#progressBar').css("width", percentage + "%");
        }
    }
}
