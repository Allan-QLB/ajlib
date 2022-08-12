package org.ajlib.log;

import java.time.LocalDateTime;
import java.util.Arrays;

public class LogRecord {
    private final Logger.Level level;
    private final String logName;
    private final String message;
    private final Object[] arguments;
    private final LocalDateTime time;

    public LogRecord(Logger.Level level, String name, String message, Object... arguments) {
        this.level = level;
        this.logName = name;
        this.message = message;
        this.arguments = arguments;
        this.time = LocalDateTime.now();
    }

    public Logger.Level getLevel() {
        return level;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public String getLogName() {
        return logName;
    }

    @Override
    public String toString() {
        return "LogRecord{" +
                "level=" + level +
                ", logName='" + logName + '\'' +
                ", message='" + message + '\'' +
                ", arguments=" + Arrays.toString(arguments) +
                ", time=" + time +
                '}';
    }
}
