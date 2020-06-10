/*
 * Copyright 2019 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

var dataSet = [
    {
        "id": 8091,
        "name": "Alien Collection",
        "overview": "A science fiction horror film franchise, focusing on Lieutenant Ellen Ripley (Sigourney Weaver) and her battle with an extraterrestrial life-form, commonly referred to as \"the Alien\". Produced by 20th Century Fox, the series started with the 1979 film Alien, then Aliens in 1986, Alien³ in 1992, and Alien: Resurrection in 1997.",
        "poster_path": "/iVzIeC3PbG9BtDAudpwSNdKAgh6.jpg",
        "backdrop_path": "/kB0Y3uGe9ohJa59Lk8UO9cUOxGM.jpg",
        "parts": [
            {
                "adult": false,
                "backdrop_path": "/AmR3JG1VQVxU8TfAvljUhfSFUOx.jpg",
                "genre_ids": [
                    27,
                    878
                ],
                "id": 348,
                "original_language": "English",
                "original_title": "Alien",
                "overview": "During its return to the earth, commercial spaceship Nostromo intercepts a distress signal from a distant planet. When a three-member team of the crew discovers a chamber containing thousands of eggs on the planet, a creature inside one of the eggs attacks an explorer. The entire crew is unaware of the impending nightmare set to descend upon them when the alien parasite planted inside its unfortunate host is birthed.",
                "poster_path": "/vfrQk5IPloGg1v9Rzbh2Eg3VGyM.jpg",
                "release_date": "1979",
                "title": "Alien",
                "video": false,
                "vote_average": 8.1,
                "vote_count": 9115,
                "popularity": 27.386
            },
            {
                "id": 679,
                "video": false,
                "vote_count": 6049,
                "vote_average": 7.9,
                "title": "Aliens",
                "release_date": "1986",
                "original_language": "English",
                "original_title": "Aliens",
                "genre_ids": [
                    28,
                    878,
                    53
                ],
                "backdrop_path": "/jMBpJFRtrtIXymer93XLavPwI3P.jpg",
                "adult": false,
                "overview": "When Ripley's lifepod is found by a salvage crew over 50 years later, she finds that terra-formers are on the very planet they found the alien species. When the company sends a family of colonists out to investigate her story—all contact is lost with the planet and colonists. They enlist Ripley and the colonial marines to return and search for answers.",
                "poster_path": "/xwdPTZyyBa4U3V2N0EmozTCeEAY.jpg",
                "popularity": 28.177
            },
            {
                "id": 8077,
                "video": false,
                "vote_count": 3380,
                "vote_average": 6.3,
                "title": "Alien³",
                "release_date": "1992",
                "original_language": "English",
                "original_title": "Alien³",
                "genre_ids": [
                    28,
                    27,
                    878
                ],
                "backdrop_path": "/9WO0CELoWq6tSdjqCs34Y0qek7l.jpg",
                "adult": false,
                "overview": "After escaping with Newt and Hicks from the alien planet, Ripley crash lands on Fiorina 161, a prison planet and host to a correctional facility. Unfortunately, although Newt and Hicks do not survive the crash, a more unwelcome visitor does. The prison does not allow weapons of any kind, and with aid being a long time away, the prisoners must simply survive in any way they can.",
                "poster_path": "/hlabk6APJUeihZDaSD9N6iI0f4g.jpg",
                "popularity": 23.206
            },
            {
                "id": 8078,
                "video": false,
                "vote_count": 2879,
                "vote_average": 6,
                "title": "Alien Resurrection",
                "release_date": "1997",
                "original_language": "English",
                "original_title": "Alien Resurrection",
                "genre_ids": [
                    28,
                    27,
                    878
                ],
                "backdrop_path": "/4hqxQQsBJpktbFA7ERZxAeqMVYH.jpg",
                "adult": false,
                "overview": "Two hundred years after Lt. Ripley died, a group of scientists clone her, hoping to breed the ultimate weapon. But the new Ripley is full of surprises … as are the new aliens. Ripley must team with a band of smugglers to keep the creatures from reaching Earth.",
                "poster_path": "/pMkAdCcq6brohqZisziZQg5i3Q5.jpg",
                "popularity": 19.369
            }
        ]
    }
];

let tooltipIds = [];

jQuery(function ($) {
    Handlebars.registerHelper('json', function (context) {
        return JSON.stringify(context);
    });

    $('#movies').DataTable({
        data: dataSet,
        deferRender: true,
        ordering: false,
        columns: [
            { data: 'imdbId' },
            { data: 'name' },
            { data: 'overview' }
        ],
        drawCallback : function( settings ) {
            tooltipIds.forEach(id => $(`#${id}`).tooltip());
        },
        columnDefs: [
            {
                targets: [0],
                type: 'html',
                searchable: false,
                render: function (data, type, row) {
                    if (type === 'display') {
                        row.poster_url =  'https://image.tmdb.org/t/p/w185' + row.poster_path;

                        dataSet.forEach(videos => videos.parts.forEach(video => tooltipIds.push(video.id)));

                        const plexServerCard = $("#movieCard").html();
                        const theTemplate = Handlebars.compile(plexServerCard);
                        return theTemplate(row);
                    }
                    return "";
                }
            },
            {
                targets: [1, 2, ],
                visible: false
            }
        ]
    });
});