package com.jasonhhouse.gaps.controller;


import com.jasonhhouse.gaps.Payload;
import com.jasonhhouse.gaps.properties.DiscordProperties;
import com.jasonhhouse.gaps.properties.OmbiProperties;
import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import com.jasonhhouse.gaps.service.OmbiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/ombi")
public class OmbiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);

    private final OmbiService ombiService;
    private final FileIoService fileIoService;

    public OmbiController(OmbiService ombiService, FileIoService fileIoService) {
        this.ombiService = ombiService;
        this.fileIoService = fileIoService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/save")
    @ResponseBody
    public ResponseEntity<Payload> putSaveOmbi(@RequestBody OmbiProperties ombiProperties) {
        LOGGER.info("putSaveOmbi( {} )", ombiProperties);

        try {
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setOmbiProperties(ombiProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info(Payload.OMBI_PROPERTIES_SAVE_SUCCEEDED.getReason());
            return ResponseEntity.ok().body(Payload.OMBI_PROPERTIES_SAVE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.OMBI_PROPERTIES_SAVE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.OMBI_PROPERTIES_SAVE_FAILED.setExtras(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            value = "/test")
    @ResponseBody
    public ResponseEntity<Payload> putTestOmbi(@RequestBody OmbiProperties ombiProperties) {
        LOGGER.info("putTestOmbi( {} )", ombiProperties);

        try {
            //ToDo
            PlexProperties plexProperties = fileIoService.readProperties();
            plexProperties.setOmbiProperties(ombiProperties);
            fileIoService.writeProperties(plexProperties);
            LOGGER.info(Payload.OMBI_PROPERTIES_SAVE_SUCCEEDED.getReason());
            return ResponseEntity.ok().body(Payload.OMBI_PROPERTIES_SAVE_SUCCEEDED);
        } catch (Exception e) {
            LOGGER.error(Payload.OMBI_PROPERTIES_SAVE_FAILED.getReason(), e);
            return ResponseEntity.ok().body(Payload.OMBI_PROPERTIES_SAVE_FAILED.setExtras(e.getMessage()));
        }
    }
}
