"use strict";

function start() {

}

function onSubmitGapsSearch() {
    var gaps = {
        movieDbApiKey: $('#movie_db_api_key').val(),
        writeToFile: true,
        movieDbListId: $('#movie_db_list_id').val(),
        searchFromPlex: true,
        connectTimeout: $('#connect_timeout').val(),
        writeTimeout: $('#write_timeout').val(),
        readTimeout: $('#read_timeout').val(),
        movieUrls: []
    };

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/submit",
        data: JSON.stringify(gaps),
        contentType:"application/json"
    });
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
        $("#folder_directory").prop('disabled', true);
        $("#folder_recursive").attr("disabled", true);
        $("#movie_formats").prop('disabled', true);
        $("#folder_regex").prop('disabled', true);
    }
}
