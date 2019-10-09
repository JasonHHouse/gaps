package com.jasonhhouse.Gaps;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class GapsUrlGeneratorTest implements UrlGenerator {

    private final static String GOOD_MOVIE_PLEX_XML = "<MediaContainer><Video title=\"Alien\" year=\"1979\"></Video></MediaContainer>";
    private final static String GOOD_ALIEN_SEARCH_RESPONSE = "{\"page\":1,\"total_results\":4,\"total_pages\":1,\"results\":[{\"popularity\":33.25,\"vote_count\":8002,\"video\":false,\"poster_path\":\"\\/2h00HrZs89SL3tXB4nbkiM7BKHs.jpg\",\"id\":348,\"adult\":false,\"backdrop_path\":\"\\/dfNrZ82poQ8blHWJreIv6JZQ9JA.jpg\",\"original_language\":\"en\",\"original_title\":\"Alien\",\"genre_ids\":[27,878],\"title\":\"Alien\",\"vote_average\":8.1,\"overview\":\"During its return to the earth, commercial spaceship Nostromo intercepts a distress signal from a distant planet. When a three-member team of the crew discovers a chamber containing thousands of eggs on the planet, a creature inside one of the eggs attacks an explorer. The entire crew is unaware of the impending nightmare set to descend upon them when the alien parasite planted inside its unfortunate host is birthed.\",\"release_date\":\"1979-05-25\"},{\"popularity\":0.975,\"id\":454788,\"video\":false,\"vote_count\":1,\"vote_average\":8,\"title\":\"Giger's Alien\",\"release_date\":\"1979-01-01\",\"original_language\":\"en\",\"original_title\":\"Giger's Alien\",\"genre_ids\":[99],\"backdrop_path\":null,\"adult\":false,\"overview\":\"Documentary about Giger's work for the movie Alien (1979).\",\"poster_path\":\"\\/dnfPMkMbrPBAmhL4jbjDtQg2KWm.jpg\"},{\"popularity\":0.6,\"id\":98401,\"video\":false,\"vote_count\":1,\"vote_average\":1,\"title\":\"The Alien Encounters\",\"release_date\":\"1979-01-01\",\"original_language\":\"en\",\"original_title\":\"The Alien Encounters\",\"genre_ids\":[878],\"backdrop_path\":null,\"adult\":false,\"overview\":\"Investigator searches for probe sent to Earth by aliens.\",\"poster_path\":\"\\/ZTg0qakePuhc4uru8RChsUNXzj.jpg\"},{\"popularity\":2.564,\"id\":42542,\"video\":false,\"vote_count\":6,\"vote_average\":2.3,\"title\":\"Starship Invasions\",\"release_date\":\"1977-10-14\",\"original_language\":\"en\",\"original_title\":\"Starship Invasions\",\"genre_ids\":[878],\"backdrop_path\":\"\\/k2dlgkyIyJAhC5CtRP2I4uVAJxr.jpg\",\"adult\":false,\"overview\":\"Captain Rameses and his Legion of the Winged Serpent brigade are out to claim Earth for their dying race. Out to save Earth is an alien guard patrol located in the Bermuda Triangle, the League of Races. LOR leaders warn Rameses that he's breaking galactic treaty rules. The alien villain responds by launching an invasion which telepathically drives Earthlings to suicide. The LOR implore UFO expert Professor Duncan to help them. Eventually, the two alien forces battle. Will the Earth be saved?\",\"poster_path\":\"\\/9Q0lbq4L7V67AfePhTPRcK9LEz7.jpg\"}]}";
    private static final String GOOD_ALIEN_DETAIL_RESPONSE = "{\"adult\":false,\"backdrop_path\":\"/dfNrZ82poQ8blHWJreIv6JZQ9JA.jpg\",\"belongs_to_collection\":{\"id\":8091,\"name\":\"Alien Collection\",\"poster_path\":\"/iVzIeC3PbG9BtDAudpwSNdKAgh6.jpg\",\"backdrop_path\":\"/kB0Y3uGe9ohJa59Lk8UO9cUOxGM.jpg\"},\"budget\":11000000,\"genres\":[{\"id\":27,\"name\":\"Horror\"},{\"id\":878,\"name\":\"Science Fiction\"}],\"homepage\":\"https://www.facebook.com/alienanthology/\",\"id\":348,\"imdb_id\":\"tt0078748\",\"original_language\":\"en\",\"original_title\":\"Alien\",\"overview\":\"During its return to the earth, commercial spaceship Nostromo intercepts a distress signal from a distant planet. When a three-member team of the crew discovers a chamber containing thousands of eggs on the planet, a creature inside one of the eggs attacks an explorer. The entire crew is unaware of the impending nightmare set to descend upon them when the alien parasite planted inside its unfortunate host is birthed.\",\"popularity\":33.25,\"poster_path\":\"/2h00HrZs89SL3tXB4nbkiM7BKHs.jpg\",\"production_companies\":[{\"id\":19747,\"logo_path\":null,\"name\":\"Brandywine Productions\",\"origin_country\":\"\"},{\"id\":25,\"logo_path\":\"/qZCc1lty5FzX30aOCVRBLzaVmcp.png\",\"name\":\"20th Century Fox\",\"origin_country\":\"US\"}],\"production_countries\":[{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"},{\"iso_3166_1\":\"GB\",\"name\":\"United Kingdom\"}],\"release_date\":\"1979-05-25\",\"revenue\":104931801,\"runtime\":117,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"},{\"iso_639_1\":\"es\",\"name\":\"Español\"}],\"status\":\"Released\",\"tagline\":\"In space no one can hear you scream.\",\"title\":\"Alien\",\"video\":false,\"vote_average\":8.1,\"vote_count\":8002}";
    private static final String GOOD_ALIEN_COLLECTION_RESPONSE = "{\"id\":8091,\"name\":\"Alien Collection\",\"overview\":\"A science fiction horror film franchise, focusing on Lieutenant Ellen Ripley (Sigourney Weaver) and her battle with an extraterrestrial life-form, commonly referred to as \\\"the Alien\\\". Produced by 20th Century Fox, the series started with the 1979 film Alien, then Aliens in 1986, Alien³ in 1992, and Alien: Resurrection in 1997.\",\"poster_path\":\"/iVzIeC3PbG9BtDAudpwSNdKAgh6.jpg\",\"backdrop_path\":\"/kB0Y3uGe9ohJa59Lk8UO9cUOxGM.jpg\",\"parts\":[{\"id\":348,\"video\":false,\"vote_count\":8002,\"vote_average\":8.1,\"title\":\"Alien\",\"release_date\":\"1979-05-25\",\"original_language\":\"en\",\"original_title\":\"Alien\",\"genre_ids\":[27,878],\"backdrop_path\":\"/dfNrZ82poQ8blHWJreIv6JZQ9JA.jpg\",\"adult\":false,\"overview\":\"During its return to the earth, commercial spaceship Nostromo intercepts a distress signal from a distant planet. When a three-member team of the crew discovers a chamber containing thousands of eggs on the planet, a creature inside one of the eggs attacks an explorer. The entire crew is unaware of the impending nightmare set to descend upon them when the alien parasite planted inside its unfortunate host is birthed.\",\"poster_path\":\"/2h00HrZs89SL3tXB4nbkiM7BKHs.jpg\",\"popularity\":33.25},{\"id\":679,\"video\":false,\"vote_count\":5363,\"vote_average\":7.9,\"title\":\"Aliens\",\"release_date\":\"1986-07-18\",\"original_language\":\"en\",\"original_title\":\"Aliens\",\"genre_ids\":[28,878,53],\"backdrop_path\":\"/gXUdQLbo6eqaTby2cEjFQW1jyVV.jpg\",\"adult\":false,\"overview\":\"When Ripley's lifepod is found by a salvage crew over 50 years later, she finds that terra-formers are on the very planet they found the alien species. When the company sends a family of colonists out to investigate her story—all contact is lost with the planet and colonists. They enlist Ripley and the colonial marines to return and search for answers.\",\"poster_path\":\"/nORMXEkYEbzkU5WkMWMgRDJwjSZ.jpg\",\"popularity\":25.819},{\"id\":8077,\"video\":false,\"vote_count\":2970,\"vote_average\":6.3,\"title\":\"Alien³\",\"release_date\":\"1992-05-22\",\"original_language\":\"en\",\"original_title\":\"Alien³\",\"genre_ids\":[28,27,878],\"backdrop_path\":\"/nDPuIjpJyGDOnNEby5ZgkRBxymS.jpg\",\"adult\":false,\"overview\":\"After escaping with Newt and Hicks from the alien planet, Ripley crash lands on Fiorina 161, a prison planet and host to a correctional facility. Unfortunately, although Newt and Hicks do not survive the crash, a more unwelcome visitor does. The prison does not allow weapons of any kind, and with aid being a long time away, the prisoners must simply survive in any way they can.\",\"poster_path\":\"/p7mUd9GpmCYV5qhkKGmiEerFK3i.jpg\",\"popularity\":20.429},{\"adult\":false,\"backdrop_path\":\"/1F8KilfgVwWTB1tIzfwoKUvPqpl.jpg\",\"genre_ids\":[878,27,28],\"id\":8078,\"original_language\":\"en\",\"original_title\":\"Alien Resurrection\",\"overview\":\"Two hundred years after Lt. Ripley died, a group of scientists clone her, hoping to breed the ultimate weapon. But the new Ripley is full of surprises … as are the new aliens. Ripley must team with a band of smugglers to keep the creatures from reaching Earth.\",\"poster_path\":\"/2mavulZivbQa8XZlRA6h3hmesl7.jpg\",\"release_date\":\"1997-11-12\",\"title\":\"Alien Resurrection\",\"video\":false,\"vote_average\":6,\"vote_count\":2523,\"popularity\":17.615}]}";
    private static final String plexMovieUrl = "/movie/url/1";
    private static final String movieDbSearchUrl = "/movieDbSearch/alien";
    private static final String movieDetailUrl = "/movieDetail/alien";
    private static final String collectionUrl = "/collection/alien";
    private MockWebServer mockWebServer;


    public GapsUrlGeneratorTest() {
    }

    public void setMockWebServer(MockWebServer mockWebServer) {
        this.mockWebServer = mockWebServer;
    }

    public void setupServer() {
        MockResponse plexResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_MOVIE_PLEX_XML);

        MockResponse movieSearchResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_ALIEN_SEARCH_RESPONSE);

        MockResponse movieDetailResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_ALIEN_DETAIL_RESPONSE);

        MockResponse collectionResponse = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody(GOOD_ALIEN_COLLECTION_RESPONSE);

        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) {

                switch (request.getPath()) {
                    case plexMovieUrl:
                        return plexResponse;
                    case movieDbSearchUrl:
                        return movieSearchResponse;
                    case movieDetailUrl:
                        return movieDetailResponse;
                    case collectionUrl:
                        return collectionResponse;
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        mockWebServer.setDispatcher(dispatcher);
    }

    HttpUrl getPlexUrl() {
        return mockWebServer.url(plexMovieUrl);
    }

    @Override
    public @NotNull HttpUrl generateSearchMovieUrl(String movieDbKey, String query, String year) {
        return mockWebServer.url(movieDbSearchUrl);
    }

    @Override
    public @NotNull HttpUrl generateMovieDetailUrl(String movieDbKey, String movieId) {
        return mockWebServer.url(movieDetailUrl);
    }

    @Override
    public @NotNull HttpUrl generateCollectionUrl(String movieDbKey, String collectionId) {
        return mockWebServer.url(collectionUrl);
    }
}
