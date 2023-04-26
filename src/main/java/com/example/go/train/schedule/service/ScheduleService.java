package com.example.go.train.schedule.service;

import com.example.go.train.schedule.domain.Schedule;
import com.example.go.train.schedule.repository.ScheduleRepository;
import com.example.go.train.schedule.util.DepartureTimeVerifier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DepartureTimeVerifier departureTimeVerifier;

    @Cacheable("schedules")
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Cacheable(value = "schedules", key = "'schedule'+#line")
    public List<Schedule> findByLineIgnoreCase(String line) {
        return scheduleRepository.findByLineIgnoreCase(line);
    }

    public Optional<List<Schedule>> filterByDepartureTime(List<Schedule> schedules, String departure) {
        if (StringUtils.isEmpty(departure)) {
            return Optional.of(schedules);
        }

        // check departure time is in correct format
        Optional<String> optionalDepartureTime = departureTimeVerifier.verify(departure);

        // filter schedules to match departure time if valid departure time
        return optionalDepartureTime.map(departureTime -> schedules.stream()
                .filter(schedule -> departureTime.equals(schedule.getDeparture().toString()))
                .collect(Collectors.toList()));
    }

}
