package com.jasonhhouse.gaps.service;

import com.jasonhhouse.gaps.sql.MediaContainerRepository;
import com.jasonhhouse.plex.video.MediaContainer;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MediaContainerService {
    private final MediaContainerRepository mediaContainerRepository;

    public MediaContainerService(MediaContainerRepository mediaContainerRepository) {
        this.mediaContainerRepository = mediaContainerRepository;
    }

    public List<MediaContainer> list() {
        return mediaContainerRepository.findAll();
    }

    public void saveAll(List<MediaContainer> mediaContainers) {
        mediaContainerRepository.saveAll(mediaContainers);
    }

    public void save(MediaContainer mediaContainer) {
        mediaContainerRepository.save(mediaContainer);
    }

    public void deleteAll() {
        mediaContainerRepository.deleteAll();
    }

}
