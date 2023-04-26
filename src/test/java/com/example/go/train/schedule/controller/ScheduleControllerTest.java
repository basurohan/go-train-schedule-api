package com.example.go.train.schedule.controller;

import com.example.go.train.schedule.domain.Schedule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScheduleControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "http://localhost:";

    @Test
    void getAllSchedules() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(17, response.getBody().size());
    }

    @Test
    void getSchedulesByLine200() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Lakeshore", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().size());
    }

    @Test
    void getSchedulesByLine404() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Niagara", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getSchedulesByLineWithValid24HourDeparture() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Lakeshore?departure=800", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getSchedulesByLineWithValid12HourDeparture() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Lakeshore?departure=8:00am", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getSchedulesByLineWithDeparture404() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Niagara?departure=800", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getSchedulesByLineWithInValid24HourDeparture() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Lakeshore?departure=80", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getSchedulesByLineWithInValid12HourDeparture() {
        ResponseEntity<List<Schedule>> response = restTemplate.exchange(
                baseUrl + port + "/schedule/Lakeshore?departure=8am", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Schedule>>() {});

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}