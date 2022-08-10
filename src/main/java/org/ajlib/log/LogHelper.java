package org.ajlib.log;


import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class LogHelper {
    private static final Map<String, Logger> CACHED = new HashMap<>();
    public static Logger getLogger(@Nonnull String name) {
        return CACHED.computeIfAbsent(name, LoggerImpl::new);
    }

    public static Logger getLogger(@Nonnull Class<?> clazz) {
        return getLogger(clazz.getName());
    }

}

