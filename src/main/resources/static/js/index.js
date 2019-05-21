"use strict";

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
