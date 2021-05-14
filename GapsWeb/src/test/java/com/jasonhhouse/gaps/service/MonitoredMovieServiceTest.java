package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.MonitoredMovie;
import com.jasonhhouse.gaps.sql.MonitoredMovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MonitoredMovieServiceTest {

    @Autowired
    private MonitoredMovieRepository monitoredMovieRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(monitoredMovieRepository).isNotNull();
    }

    @Test
    public void testAddingMonitored() {
        MonitoredMovie monitoredMovie = new MonitoredMovie(123, false);
        monitoredMovieRepository.save(monitoredMovie);
        MonitoredMovie monitoredMovie1 = monitoredMovieRepository.findMonitoredMovieByTmdbId(123);
        assertEquals(monitoredMovie, monitoredMovie1, "Should save and find monitored movie");
    }

    @Test
    public void testUpdateMonitoredMovie() {
        MonitoredMovie monitoredMovie = new MonitoredMovie(123, false);
        monitoredMovieRepository.save(monitoredMovie);
        monitoredMovie = monitoredMovieRepository.findMonitoredMovieByTmdbId(123);
        monitoredMovie.setMonitored(true);
        monitoredMovieRepository.save(monitoredMovie);
        MonitoredMovie monitoredMovie1 = monitoredMovieRepository.findMonitoredMovieByTmdbId(123);
        assertEquals(monitoredMovie, monitoredMovie1, "Should save and update monitored movie");
    }

}