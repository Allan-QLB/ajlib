package org.ajlib.log;


import org.ajlib.util.ConfigUtil;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;

public class LoggerFactory {
    private static final Map<String, Logger> CACHED = new HashMap<>();
    private static final String LOG_CFG_FILE = "config-log.yaml";
    private static List<LogHandler> logHandlers = null;

    static {
        initHandler();
    }

    @SuppressWarnings("unchecked")
    public static void initHandler() {
        List<LogHandler> handlers = new ArrayList<>();
        try {
            LogHandler handler = null;
            final Map<String, Object> config = ConfigUtil.loadConfigAsMap(LOG_CFG_FILE);
            for (LogType t : LogType.values()) {
                final String logTypeName = t.toString().toLowerCase();
                if (config.containsKey(logTypeName)) {
                    handler = t.handlerFactory.apply((Map<String, Object>) Optional.ofNullable(config.get(logTypeName))
                                    .orElse(Collections.emptyMap()));
                }
                if (handler != null) {
                    handlers.add(handler);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (handlers.isEmpty()) {
                handlers.add(new ConsoleHandler());
            }
            logHandlers = Collections.unmodifiableList(handlers);
        }
    }

    public static Logger getLogger(@Nonnull String name) {
        return CACHED.computeIfAbsent(name, k -> new LoggerImpl(k, logHandlers));
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

