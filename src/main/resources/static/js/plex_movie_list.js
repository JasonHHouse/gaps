"use strict"

let stompClient;
let backButton;
let copyToClipboard;
let searchResults;
let searchPosition;
let progressContainer;
let searchTitle;
let searchDescription;

document.addEventListener('DOMContentLoaded', function () {
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems);

    searchResults = $('#searchResults');
    backButton = $('#cancel');
    copyToClipboard = $('#copyToClipboard');
    searchResults = $('#searchResults');
    searchPosition = $('#searchPosition');
    progressContainer = $('#progressContainer');
    searchTitle = $('#searchTitle');
    searchDescription = $('#searchDescription');

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

window.onbeforeunload = function () {
    disconnect();
}

function setCopyToClipboardEnabled(bool) {
    if (bool) {
        copyToClipboard.removeClass('disabled');
    } else {
        copyToClipboard.addClass('disabled');
    }
}

function search() {

    progressContainer.hide();
    searchTitle.text("Searching for Movies...");
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
    };

    $.ajax({
        type: "POST",
        url: "http://" + location.hostname + ":" + location.port + "/submit",
        data: JSON.stringify(gaps),
        contentType: "application/json",
        timeout: 0,
        success: function (movies) {
            let movieHtml = "";
            movies.forEach(function (movie) {
                movieHtml += buildMovieDiv(movie);
            });

            searchTitle.text(movies.length + ' movies to add to complete your collections');
            searchDescription.text("Below is everything Gaps found that is missing from your movie collections.");
            progressContainer.hide();
            backButton.text('restart');
            setCopyToClipboardEnabled(true);
        },
        error: function (err) {
            disconnect();
            let message = "Unknown error. Check docker Gaps log file.";
            if (err) {
                message = JSON.parse(err.responseText).message;
                Console.error(message);
            }

            searchTitle.text("An error occurred...");
            searchDescription.text("");
            progressContainer.hide();
            backButton.text('restart');
            setCopyToClipboardEnabled(false);
        }
    });

    showSearchStatus();
}

function buildMoviesDiv(movies) {
    let result = '';

    for (let movie of movies) {
        result += buildMovieDiv(movie);
    }

    return result;
}

function buildMovieDiv(movie) {
    return '<div>' + buildMovie(movie) + '</div>';
}

function buildMovie(movie) {
    return movie.name + " (" + movie.year + ") from '" + movie.collection + "'";
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/currentSearchResults', function (status) {
            let obj = JSON.parse(status.body);
            showSearchStatus(obj);
            shouldDisconnect(obj)
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function shouldDisconnect(obj) {
    if (obj && obj.searchedMovieCount && obj.totalMovieCount && obj.totalMovieCount === obj.searchedMovieCount) {
        disconnect();

        //Cancel Search
        $.ajax({
            type: "PUT",
            url: "http://" + location.hostname + ":" + location.port + "/cancelSearch",
            contentType: "application/json",
        });
    }
}

function showSearchStatus(obj) {
    if (!obj || (!obj.searchedMovieCount && !obj.totalMovieCount && obj.totalMovieCount === 0)) {
        searchResults.html("");
    } else {
        progressContainer.show();
        let percentage = Math.trunc(obj.searchedMovieCount / obj.totalMovieCount * 100);
        searchPosition.html('<h5>' + obj.searchedMovieCount + ' of ' + obj.totalMovieCount + ' movies searched. ' + percentage + '% complete.</h5>');

        searchResults.html(buildMoviesDiv(obj.moviesFound));

        $('#progressBar').css("width", percentage + "%");
    }
}

function encodeQueryData(data) {
    const ret = [];
    for (let d in data) {
        ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    }
    return ret.join('&');
}

function CopyToClipboard(containerid) {
    let range;
    if (document.selection) {
        range = document.body.createTextRange();
        range.moveToElementText(document.getElementById(containerid));
        range.select().createTextRange();
        document.execCommand("copy");

    } else if (window.getSelection) {
        range = document.createRange();
        range.selectNode(document.getElementById(containerid));
        window.getSelection().addRange(range);
        document.execCommand("copy");
    }
}