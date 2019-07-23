"use strict"

let keepChecking;
let stompClient;
let backButton;
let copyToClipboard;
let searchResults = $('#searchResults');

document.addEventListener('DOMContentLoaded', function () {
    keepChecking = true;

    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems);

    backButton = $('#cancel');
    copyToClipboard = $('#copyToClipboard');
    searchResults = $('#searchResults');

    backButton.click(function () {
        $('#warningModal').modal('open');
    });

    setCopyToClipboardEnabled(false);
    copyToClipboard.click(function () {
        CopyToClipboard('searchResults');
        M.toast({ html: 'Copied to Clipboard' });
    });

    $('#agree').click(function () {
        //Cancel Search
        $.ajax({
            type: "PUT",
            url: "http://" + location.hostname + ":" + location.port + "/cancelSearch",
            contentType: "application/json",
        });

        //Navigate Home
        location.assign("index.html");
    });

    connect();
    search();
});

function setCopyToClipboardEnabled(bool) {
    if (bool) {
        copyToClipboard.removeClass('disabled');
    } else {
        copyToClipboard.addClass('disabled');
    }
}

function search() {
    let searchTitle = $('#searchTitle');
    let progressContainer = $('#progressContainer');

    let searchDescription = $('#searchDescription');

    progressContainer.hide();
    searchTitle.text("Searching...");
    searchDescription.text("Gaps is looking through your Plex libraries. This could take a while so just sit tight and we'll find all the missing movies for you.");

    let obj = JSON.parse(document.cookie);

    let plexMovieUrls = [];

    for (let library of obj.libraries) {
        let data = {
            'X-Plex-Token': obj.plex_token
        };

        let plexMovieUrl = "http://" + obj.address + ":" + obj.port + "/library/sections/" + library.key + "/all/?" + encodeQueryData(data);
        plexMovieUrls.push(plexMovieUrl);
    }

    const gaps = {
        movieDbApiKey: obj.movie_db_api_key,
        writeToFile: true,
        searchFromPlex: true,
        movieUrls: plexMovieUrls
    }

    $.ajax({
        type: "POST",
        url: "http://" + location.hostname + ":" + location.port + "/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function (movies) {
            keepChecking = false;
            let movieHtml = "";
            movies.forEach(function (movie) {
                movieHtml += buildMovieDiv(movie);
            });

            searchTitle.text(movies.length + ' movies to add to complete your collections');
            searchDescription.text("Below is everything Gaps found that is missing from your movie collections.");
            progressContainer.hide();
            backButton.text('restart');

            searchResults.html(movieHtml);

            setCopyToClipboardEnabled(true);

            disconnect();
        },
        error: function (err) {
            let message = "Unknown error. Check docker Gaps log file.";
            if (err) {
                message = JSON.parse(err.responseText).message;
            }

            searchTitle.text("An error occurred...");
            searchDescription.text("");
            progressContainer.hide();

            searchResults.html(message);
            keepChecking = false;
            backButton.text('restart');

            setCopyToClipboardEnabled(false);

            disconnect();
        }
    });

    showSearchStatus();
}

function buildMovieDiv(movie) {
    return '<div>' + buildMovie(movie) + '</div>';
}

function buildMovie(movie) {
    return movie.name + " (" + movie.year + ") from '" + movie.collection + "'";
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/currentSearchResults', function (status) {
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
        if (!obj || (!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0)) {
            searchResults.html("<p>Searching for movies...</p>");
        } else {
            $('#progressContainer').show();
            let percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
            $('#searchPosition').text(obj.searchedMovieCount + ' of ' + obj.totalMovieCount + " movies searched. " + percentage + "% complete.");

            for (let movie of obj.moviesFound) {
                searchResults.append(buildMovieDiv(movie));
            }

            $('#progressBar').css("width", percentage + "%");
        }
    }
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data)
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    return ret.join('&');
}

function CopyToClipboard(containerid) {
    if (document.selection) {
        var range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerid));
        range.select().createTextRange();
        document.execCommand("copy");

    } else if (window.getSelection) {
        var range = document.createRange();
        range.selectNode(document.getElementById(containerid));
        window.getSelection().addRange(range);
        document.execCommand("copy");
    }
}