package org.ajlib.log;

import java.util.Collections;

public class ConsoleLog {
    private static final Logger console = new LoggerImpl("default", Collections.singletonList(new ConsoleHandler()));

    public static boolean isDebugEnabled() {
        return console.isDebugEnabled();
    }

    public static void debug(String message, Object... args) {
        console.debug(message, args);
    }

    public static void info(String message, Object... args) {
        console.info(message, args);
    }

    public static void warn(String message, Object... args) {
        console.warn(message, args);
    }

    public static void error(String message, Object... args) {
        console.error(message, args);
    }
}
