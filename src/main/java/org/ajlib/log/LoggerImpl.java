package org.ajlib.log;

import javax.annotation.Nonnull;

import static org.ajlib.log.Logger.Level.*;

public class LoggerImpl implements Logger {

    private final String name;

    private final LogHandler handler;

    private boolean debugEnable;

    public LoggerImpl(@Nonnull String name, @Nonnull LogHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String message, Object... args) {
        if (isDebugEnabled()) {
            write(DEBUG, message, args);
        }
    }

    @Override
    public void info(String message, Object... args) {
        write(INFO, message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        write(WARN, message, args);
    }

    @Override
    public void error(String message, Object... args) {
        write(ERROR, message, args);
    }

    private void write(Level level, String message, Object... args) {
        handler.handleLog(new LogRecord(level, name, message, args));
    }

}
