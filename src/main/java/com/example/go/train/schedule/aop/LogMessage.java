package com.example.go.train.schedule.aop;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LogMessage {
    private String className;
    private String methodName;
    private String methodArgs;
    private Long elapsedTimeInMills;
    private Long elapsedTimeInMicros;
    private Object result;
}
