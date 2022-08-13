package org.ajlib.log;

import javax.annotation.Nonnull;
import java.util.List;

import static org.ajlib.log.Logger.Level.*;

public class LoggerImpl implements Logger {

    private final String name;

    private final List<LogHandler> handlers;

    private boolean debugEnable;

    public LoggerImpl(@Nonnull String name, List<LogHandler> handlers) {
        this.name = name;
        this.handlers = handlers;
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
        final LogRecord log = new LogRecord(level, name, message, args);
        for (LogHandler handler : handlers) {
            handler.handleLog(log);
        }
    }
}
