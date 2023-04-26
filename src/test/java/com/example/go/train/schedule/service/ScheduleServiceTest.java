package com.example.go.train.schedule.service;

import com.example.go.train.schedule.domain.Schedule;
import com.example.go.train.schedule.repository.ScheduleRepository;
import com.example.go.train.schedule.util.DepartureTimeVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduleServiceTest {

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    DepartureTimeVerifier departureTimeVerifier;

    @InjectMocks
    private ScheduleService scheduleService;

    List<Schedule> schedules = new ArrayList<>();

    @BeforeEach
    void setUp() {
        schedules.add(Schedule.builder().id(1).line("Lakeshore").departure(800).arrival(900).build());
        schedules.add(Schedule.builder().id(2).line("Barrie").departure(1000).arrival(1100).build());
    }

    @Test
    void testFindAll() {
        // Given
        when(scheduleRepository.findAll()).thenReturn(schedules);

        // When
        List<Schedule> result = scheduleService.findAll();

        // Then
        assertEquals(schedules.size(), result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Lakeshore", result.get(0).getLine());
        assertEquals(2, result.get(1).getId());
        assertEquals("Barrie", result.get(1).getLine());
    }

    @Test
    void testFindByLineIgnoreCase() {
        // Given
        List<Schedule> filteredList = new ArrayList<>();
        filteredList.add(Schedule.builder().id(1).line("Lakeshore").departure(800).arrival(900).build());
        when(scheduleRepository.findByLineIgnoreCase("Lakeshore")).thenReturn(filteredList);

        // When
        List<Schedule> result = scheduleService.findByLineIgnoreCase("Lakeshore");

        // Then
        assertEquals(filteredList.size(), result.size());
        verify(scheduleRepository, times(1)).findByLineIgnoreCase("Lakeshore");
    }

    @Test
    void testFindByLineIgnoreCaseEmptyResult() {
        // Given
        when(scheduleRepository.findByLineIgnoreCase("Barrie")).thenReturn(Collections.emptyList());

        // When
        List<Schedule> result = scheduleService.findByLineIgnoreCase("Barrie");

        // Then
        assertEquals(0, result.size());
        verify(scheduleRepository, times(1)).findByLineIgnoreCase("Barrie");
    }

    @Test
    void testFilterByDepartureTimeNullDepartureValue() {
        // When
        Optional<List<Schedule>> result = scheduleService.filterByDepartureTime(schedules, null);

        // Then
        assertTrue(result.isPresent());
        assertEquals(schedules.size(), result.get().size());
    }

    @Test
    void testFilterByDepartureTimeEmptyResponse() {
        // Given
        when(departureTimeVerifier.verify(anyString())).thenReturn(Optional.empty());
        // When
        Optional<List<Schedule>> result = scheduleService.filterByDepartureTime(schedules, "800");
        // Then
        assertFalse(result.isPresent());
        verify(departureTimeVerifier, times(1)).verify("800");
    }

    @Test
    void testFilterByDepartureTimeValidResponse() {
        // Given
        when(departureTimeVerifier.verify(anyString())).thenReturn(Optional.of("800"));
        // When
        Optional<List<Schedule>> result = scheduleService.filterByDepartureTime(schedules, "800");
        // Then
        assertTrue(result.isPresent());
        assertEquals(800, result.get().get(0).getDeparture());
        verify(departureTimeVerifier, times(1)).verify("800");
    }

}