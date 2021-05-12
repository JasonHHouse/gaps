package com.jasonhhouse.gaps.sql;

import com.jasonhhouse.gaps.MonitoredMovie;
import org.springframework.data.repository.CrudRepository;

public interface MonitoredMovieRepository extends CrudRepository<MonitoredMovie, Long> {

}
