package org.ajlib.log;

public interface Logger {
    boolean isDebugEnabled();
    void debug(String message, Object... args);
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Object... args);



}
