package org.ajlib.log;

import java.io.PrintStream;

import static org.ajlib.log.Logger.Level.*;

public class LoggerImpl implements Logger {

    private final String name;

    private boolean debugEnable;

    public LoggerImpl(String name) {
        this.name = name;
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
        write(new LogRecord(level, name, message, args), level == ERROR ? System.err : System.out);
    }

    private void write(LogRecord log, PrintStream target) {
        if (log != null) {
           target.println(log.textify());
        }
    }

}
