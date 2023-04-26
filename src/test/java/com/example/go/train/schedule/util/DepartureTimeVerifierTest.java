package com.example.go.train.schedule.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartureTimeVerifierTest {

    DepartureTimeVerifier departureTimeVerifier = new DepartureTimeVerifier();

    @Test
    void testVerifyNullValue() {
        // Then
        assertEquals(Optional.empty(), departureTimeVerifier.verify(null));
    }

    @Test
    void testVerifyInvalidDeparture() {
        // Then
        assertEquals(Optional.empty(), departureTimeVerifier.verify("aaaa"));
        assertEquals(Optional.empty(), departureTimeVerifier.verify("22"));
        assertEquals(Optional.empty(), departureTimeVerifier.verify("6am"));
    }

    @Test
    void testVerify24HourFormat() {
        // When
        Optional<String> result = departureTimeVerifier.verify("1215");

        // Then
        assertEquals("1215", result.get());
    }

    @Test
    void testVerify12HourFormatAm() {
        // When
        Optional<String> result = departureTimeVerifier.verify("6:00am");

        // Then
        assertEquals("600", result.get());
    }

    @Test
    void testVerify12HourFormatPm() {
        // When
        Optional<String> result = departureTimeVerifier.verify("6:00pm");

        // Then
        assertEquals("1800", result.get());
    }

}