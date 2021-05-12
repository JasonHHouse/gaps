package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.MonitoredMovie;
import com.jasonhhouse.gaps.sql.MonitoredMovieRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MonitoredMovieService {
    private final MonitoredMovieRepository monitoredMovieRepository;

    public MonitoredMovieService(MonitoredMovieRepository monitoredMovieRepository) {
        this.monitoredMovieRepository = monitoredMovieRepository;
    }

    public List<MonitoredMovie> list() {
        List<MonitoredMovie> result = new ArrayList<>();
        monitoredMovieRepository.findAll().forEach(result::add);
        return result;
    }

    public void saveAll(List<MonitoredMovie> monitoredMovies) {
        monitoredMovieRepository.saveAll(monitoredMovies);
    }

    public void save(MonitoredMovie monitoredMovie) {
        monitoredMovieRepository.save(monitoredMovie);
    }

    public void deleteAll() {
        monitoredMovieRepository.deleteAll();
    }

}
