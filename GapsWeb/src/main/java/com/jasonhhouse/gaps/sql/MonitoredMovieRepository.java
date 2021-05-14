package com.jasonhhouse.gaps.sql;

import com.jasonhhouse.gaps.MonitoredMovie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MonitoredMovieRepository extends CrudRepository<MonitoredMovie, Long> {

    @Query("SELECT mm FROM MonitoredMovie mm WHERE mm.tmdbId = ?1")
    MonitoredMovie findMonitoredMovieByTmdbId(Integer tmdbId);

}
