package com.example.go.train.schedule;

import com.example.go.train.schedule.domain.Schedule;
import com.example.go.train.schedule.repository.ScheduleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Go Train Schedule API", version = "1.0",
		description = "Go Train Schedule API Microservice"))
@EnableCaching
@Slf4j
public class GoTrainScheduleApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoTrainScheduleApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ScheduleRepository scheduleRepository) {
		return args -> {
			log.info("Loading data to DB...");
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Schedule>> typeReference = new TypeReference<List<Schedule>>() {};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");
			try {
				List<Schedule> schedules = mapper.readValue(inputStream, typeReference);
				scheduleRepository.saveAll(schedules);
				log.info("Data loaded to DB");
			} catch (IOException ex) {
				log.error("Error loading data in DB: " + ex.getLocalizedMessage());
			}
		};
	}

}
