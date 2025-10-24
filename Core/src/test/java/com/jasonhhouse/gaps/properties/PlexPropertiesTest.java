/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.jasonhhouse.gaps.properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonhhouse.gaps.MovieStatus;
import com.jasonhhouse.gaps.PlexServer;
import com.jasonhhouse.gaps.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlexPropertiesTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDefaultConstructor() {
        PlexProperties properties = new PlexProperties();

        assertNotNull(properties.getPlexServers());
        assertTrue(properties.getPlexServers().isEmpty());
        assertEquals("", properties.getMovieDbApiKey());
        assertEquals("", properties.getPassword());
        assertEquals(Schedule.EVERY_MONDAY, properties.getSchedule());
        assertEquals(MovieStatus.ALL, properties.getMovieStatus());
        assertNotNull(properties.getTelegramProperties());
        assertNotNull(properties.getPushBulletProperties());
        assertNotNull(properties.getEmailProperties());
        assertNotNull(properties.getGotifyProperties());
        assertNotNull(properties.getSlackProperties());
        assertNotNull(properties.getPushOverProperties());
        assertNotNull(properties.getDiscordProperties());
    }

    @Test
    public void testJsonConstructorWithNullValues() throws JsonProcessingException {
        String json = "{}";
        PlexProperties properties = objectMapper.readValue(json, PlexProperties.class);

        assertNotNull(properties.getPlexServers());
        assertTrue(properties.getPlexServers().isEmpty());
        assertEquals("", properties.getMovieDbApiKey());
        assertEquals("", properties.getPassword());
        assertEquals(Schedule.EVERY_MONDAY, properties.getSchedule());
        assertEquals(MovieStatus.ALL, properties.getMovieStatus());
    }

    @Test
    public void testJsonConstructorWithAllValues() throws JsonProcessingException {
        String json = "{\"movieDbApiKey\":\"test-key\",\"password\":\"test-pass\",\"schedule\":{\"id\":0,\"message\":\"Hourly\",\"enabled\":true},\"movieStatus\":{\"id\":1,\"message\":\"Released\"}}";
        PlexProperties properties = objectMapper.readValue(json, PlexProperties.class);

        assertEquals("test-key", properties.getMovieDbApiKey());
        assertEquals("test-pass", properties.getPassword());
        assertEquals(Schedule.HOURLY, properties.getSchedule());
        assertEquals(MovieStatus.RELEASED, properties.getMovieStatus());
    }

    @Test
    public void testSetAndGetMovieDbApiKey() {
        PlexProperties properties = new PlexProperties();
        properties.setMovieDbApiKey("my-api-key");

        assertEquals("my-api-key", properties.getMovieDbApiKey());
    }

    @Test
    public void testSetAndGetPassword() {
        PlexProperties properties = new PlexProperties();
        properties.setPassword("secure-password");

        assertEquals("secure-password", properties.getPassword());
    }

    @Test
    public void testSetAndGetSchedule() {
        PlexProperties properties = new PlexProperties();
        properties.setSchedule(Schedule.DAILY_4AM);

        assertEquals(Schedule.DAILY_4AM, properties.getSchedule());
    }

    @Test
    public void testSetAndGetMovieStatus() {
        PlexProperties properties = new PlexProperties();
        properties.setMovieStatus(MovieStatus.RELEASED);

        assertEquals(MovieStatus.RELEASED, properties.getMovieStatus());
    }

    @Test
    public void testAddPlexServer() {
        PlexProperties properties = new PlexProperties();
        PlexServer server = new PlexServer();

        properties.addPlexServer(server);

        assertEquals(1, properties.getPlexServers().size());
        assertEquals(server, properties.getPlexServers().get(0));
    }

    @Test
    public void testAddMultiplePlexServers() {
        PlexProperties properties = new PlexProperties();
        PlexServer server1 = new PlexServer();
        PlexServer server2 = new PlexServer();

        properties.addPlexServer(server1);
        properties.addPlexServer(server2);

        assertEquals(2, properties.getPlexServers().size());
    }

    @Test
    public void testSetAndGetTelegramProperties() {
        PlexProperties properties = new PlexProperties();
        TelegramProperties telegram = TelegramProperties.getDefault();

        properties.setTelegramProperties(telegram);

        assertEquals(telegram, properties.getTelegramProperties());
    }

    @Test
    public void testSetAndGetPushBulletProperties() {
        PlexProperties properties = new PlexProperties();
        PushBulletProperties pushBullet = PushBulletProperties.getDefault();

        properties.setPushBulletProperties(pushBullet);

        assertEquals(pushBullet, properties.getPushBulletProperties());
    }

    @Test
    public void testSetAndGetEmailProperties() {
        PlexProperties properties = new PlexProperties();
        EmailProperties email = EmailProperties.getDefault();

        properties.setEmailProperties(email);

        assertEquals(email, properties.getEmailProperties());
    }

    @Test
    public void testSetAndGetGotifyProperties() {
        PlexProperties properties = new PlexProperties();
        GotifyProperties gotify = GotifyProperties.getDefault();

        properties.setGotifyProperties(gotify);

        assertEquals(gotify, properties.getGotifyProperties());
    }

    @Test
    public void testSetAndGetSlackProperties() {
        PlexProperties properties = new PlexProperties();
        SlackProperties slack = SlackProperties.getDefault();

        properties.setSlackProperties(slack);

        assertEquals(slack, properties.getSlackProperties());
    }

    @Test
    public void testSetAndGetPushOverProperties() {
        PlexProperties properties = new PlexProperties();
        PushOverProperties pushOver = PushOverProperties.getDefault();

        properties.setPushOverProperties(pushOver);

        assertEquals(pushOver, properties.getPushOverProperties());
    }

    @Test
    public void testSetAndGetDiscordProperties() {
        PlexProperties properties = new PlexProperties();
        DiscordProperties discord = DiscordProperties.getDefault();

        properties.setDiscordProperties(discord);

        assertEquals(discord, properties.getDiscordProperties());
    }

    @Test
    public void testToString() {
        PlexProperties properties = new PlexProperties();
        String result = properties.toString();

        assertNotNull(result);
        assertTrue(result.contains("PlexProperties"));
        assertTrue(result.contains("plexServers"));
        assertTrue(result.contains("movieDbApiKey"));
        assertTrue(result.contains("schedule"));
        assertTrue(result.contains("movieStatus"));
    }

    @Test
    public void testRoundTripSerialization() throws JsonProcessingException {
        PlexProperties original = new PlexProperties();
        original.setMovieDbApiKey("test-key");
        original.setPassword("test-pass");
        original.setSchedule(Schedule.DAILY_4AM);
        original.setMovieStatus(MovieStatus.RELEASED);

        String json = objectMapper.writeValueAsString(original);
        PlexProperties deserialized = objectMapper.readValue(json, PlexProperties.class);

        assertEquals(original.getMovieDbApiKey(), deserialized.getMovieDbApiKey());
        assertEquals(original.getPassword(), deserialized.getPassword());
        assertEquals(original.getSchedule(), deserialized.getSchedule());
        assertEquals(original.getMovieStatus(), deserialized.getMovieStatus());
    }
}
