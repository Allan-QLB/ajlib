package org.ajlib.log;


import org.ajlib.util.ConfigUtil;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class LogHelper {
    private static final Map<String, Logger> CACHED = new HashMap<>();
    private static final String LOG_CFG_FILE = "config-log.yaml";
    private static LogHandler logHandler;

    static {
        initHandler();
    }

    public static void initHandler() {
        try {
            LogHandler handler = null;
            final Map<String, Object> config = ConfigUtil.loadConfigAsMap(LOG_CFG_FILE);
            for (LogType t : LogType.values()) {
                if (config.containsKey(t.toString())) {
                    handler = t.handlerFactory.apply((Map<String, Object>) Optional.ofNullable(config.get(t.toString()))
                            .orElse(Collections.emptyMap()));
                }
                if (handler != null) {
                    logHandler = handler;
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (logHandler == null) {
                logHandler = new ConsoleHandler();
            }
        }
    }

    public static Logger getLogger(@Nonnull String name) {
        return CACHED.computeIfAbsent(name, k -> new LoggerImpl(k, logHandler));
    }

    public static Logger getLogger(@Nonnull Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    enum LogType {
        CONSOLE(cfg -> new ConsoleHandler()),
        FILE(FileHandler::new);
        private final Function<Map<String, Object>, LogHandler> handlerFactory;

        LogType(Function<Map<String, Object>, LogHandler> handlerFactory) {
            this.handlerFactory = handlerFactory;
        }
    }

}

