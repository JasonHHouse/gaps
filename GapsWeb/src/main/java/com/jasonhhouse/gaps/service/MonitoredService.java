package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.Monitored;
import com.jasonhhouse.gaps.sql.MonitoredRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MonitoredService {
    private final MonitoredRepository monitoredRepository;

    public MonitoredService(MonitoredRepository monitoredRepository) {
        this.monitoredRepository = monitoredRepository;
    }

    public List<Monitored> list() {
        return monitoredRepository.findAll();
    }

    public void saveAll(List<Monitored> monitoredList) {
        monitoredRepository.saveAll(monitoredList);
    }

    public void save(Monitored monitored) {
        monitoredRepository.save(monitored);
    }

    public void deleteAll() {
        monitoredRepository.deleteAll();
    }

}
