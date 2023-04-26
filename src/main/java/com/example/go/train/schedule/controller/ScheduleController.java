package com.example.go.train.schedule.controller;

import com.example.go.train.schedule.domain.Schedule;
import com.example.go.train.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "Schedules", description = "Go Train Schedule APIs")
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "Returns entire timetable as JSON array")
    @ApiResponse(responseCode = "200", description = "Returns entire timetable from DB",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Schedule.class)))})
    @GetMapping("/schedule")
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @Operation(summary = "Returns timetable for a line")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns timetable for a line",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Schedule.class)))}),
            @ApiResponse(responseCode = "404", description = "Line not found", content = {@Content}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {@Content})
    })
    @GetMapping("/schedule/{line}")
    public ResponseEntity<List<Schedule>> getSchedulesByLine(@PathVariable final String line,
                                                             @RequestParam (required = false) final String departure) {

        // check whether schedule is available for the line value provided
        List<Schedule> schedules = scheduleService.findByLineIgnoreCase(line);

        if (schedules.size() == 0) {
            // return not found if line does not exist
            return ResponseEntity.notFound().build();
        } else if (departure == null){
            // return schedules if departure param not provided
            return ResponseEntity.ok(schedules);
        } else {
            Optional<List<Schedule>> optionalSchedules = scheduleService.filterByDepartureTime(schedules, departure);
            return optionalSchedules.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
        }

    }

}
