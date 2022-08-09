package org.ajlib.log;


import org.ajlib.util.StringUtil;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHelper {
    private static final Map<String, FileHandler> handlerMap = new HashMap<>();

    private static final String JAR_PLUGIN_MARKER = "aj-plugin";

    public static Logger getLogger() {
        try {
            final Class<?> caller = getCallerClass(2);
            System.out.println(caller);
            final URL location = caller.getProtectionDomain().getCodeSource().getLocation();
            final File file = new File(location.getFile());
            final JarFile jar = new JarFile(file);
            final Manifest manifest = jar.getManifest();
            String pluginName = manifest.getMainAttributes().getValue(JAR_PLUGIN_MARKER);
            if (StringUtil.isEmpty(pluginName)) {
                pluginName = "default";
            }
            final Logger logger = Logger.getLogger(pluginName);
            handlerMap.computeIfAbsent(pluginName, k -> {
                final FileHandler handler;
                try {
                    handler = new FileHandler(new File(file.getParent(), "log").getPath());
                    handler.setFormatter(new SimpleFormatter());
                    logger.addHandler(handler);
                    return handler;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return logger;
        } catch (IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }

    private static Class<?> getCallerClass(int i) throws ClassNotFoundException {
        final RuntimeException exception = new RuntimeException();
        final StackTraceElement[] stackTrace = exception.getStackTrace();
        return ClassLoader.getSystemClassLoader().loadClass(stackTrace[i].getClassName());
    }
}

