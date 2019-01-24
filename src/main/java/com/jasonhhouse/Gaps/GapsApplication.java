package com.jasonhhouse.Gaps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GapsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GapsApplication.class, args);
    }

}


/*
query for all movies from plex
Build an arraylist plex movies of movie name and year
Build an empty hash set of movie objects called searched
Build an empty arraylist of recommended movie objects
Iterate through plex movie list
    if movie is in searched
        continue

    else
        search for movie

        if movie has collections

            Look up each collection
            Iterate over each collection

                if movie in plex movie
                    add movie to searched

                else
                    add to recommended

        put movie into searched

print out recommended


 */
