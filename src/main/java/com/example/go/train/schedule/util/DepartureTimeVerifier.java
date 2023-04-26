package com.example.go.train.schedule.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class DepartureTimeVerifier {

    public Optional<String> verify(String departure) {
        if (StringUtils.isEmpty(departure)) {
            return Optional.empty();
        }
        String regex_military = "^\\d{1}$|\\d{3}$|\\d{4}$";
        String regex_normal = "^([1-9]|0[1-9]|1[0-2]):[0-5][0-9]([AaPp][Mm])$";

        Pattern militaryPattern = Pattern.compile(regex_military);
        Pattern normalPattern = Pattern.compile(regex_normal);

        Matcher militaryMatcher = militaryPattern.matcher(departure);
        Matcher normalMatcher = normalPattern.matcher(departure);

        if (militaryMatcher.matches()) {
            return Optional.of(departure);
        } else if (normalMatcher.matches()) {
            String[] timeArr = departure.split(":");
            String AmPm = timeArr[1].substring(2);

            int hh = Integer.parseInt(timeArr[0]);
            int mm = Integer.parseInt(timeArr[1].substring(0,2));

            if (AmPm.equalsIgnoreCase("am") && hh == 12) {
                hh = 00;
            } else if (AmPm.equalsIgnoreCase("pm") && hh < 12) {
                hh += 12;
            }
            return Optional.of(String.format("%d%02d",hh,mm));
        }

        log.info("Invalid departure time format: " + departure);
        return Optional.empty();
    }
}
