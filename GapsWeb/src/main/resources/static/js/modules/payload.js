/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
export const Payload = Object.freeze({
    'UNKNOWN_ERROR': -1,
    'SEARCH_SUCCESSFUL': 0,
    'SEARCH_CANCELLED': 1,
    'SEARCH_FAILED': 2,
    'OWNED_MOVIES_CANNOT_BE_EMPTY': 3,
    'PLEX_CONNECTION_SUCCEEDED': 10,
    'PLEX_CONNECTION_FAILED': 11,
    'PARSING_PLEX_FAILED': 12,
    'PLEX_URL_ERROR': 13,
    'PLEX_LIBRARIES_FOUND': 13,
    'DUPLICATE_PLEX_LIBRARY': 14,
    'TMDB_KEY_VALID': 20,
    'TMDB_KEY_INVALID': 21,
    'TMDB_CONNECTION_ERROR': 22,
    'TMDB_KEY_SAVE_SUCCESSFUL': 23,
    'TMDB_KEY_SAVE_UNSUCCESSFUL': 24,
    'NUKE_SUCCESSFUL': 30,
    'NUKE_UNSUCCESSFUL': 31,
    'PLEX_LIBRARY_MOVIE_FOUND': 40,
    'PLEX_LIBRARY_MOVIE_NOT_FOUND': 41,
    'RECOMMENDED_MOVIES_FOUND': 50,
    'RECOMMENDED_MOVIES_NOT_FOUND': 51,
    'SCHEDULE_FOUND': 60,
    'SCHEDULE_NOT_FOUND': 61,
    'SCHEDULE_UPDATED': 62,
    'SCHEDULE_NOT_UPDATED': 63
});