package org.ajlib.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.ajlib.Agent;
import org.ajlib.exception.ConfigurationException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class ConfigUtil {
    private static final String CONFIG_FILE = "config.yaml";

    public static Map<String, Object> loadConfigAsMap() throws IOException {
        return loadConfigAsMap(CONFIG_FILE);
    }

    static Map<String, Object> loadConfigAsMap(String fileName) throws IOException {
        final InputStream configStream = Agent.class.getClassLoader().getResourceAsStream(fileName);
        if (configStream == null) {
            throw new ConfigurationException("Config file " + CONFIG_FILE + " not found in classpath");
        }
        final Yaml yaml = new Yaml();
        try (InputStream inputStream = configStream) {
            final Map<String, Object> configMap = yaml.loadAs(inputStream, Map.class);
            if (CollectionUtil.isEmpty(configMap)) {
                log.warn("Config is empty");
                return Collections.emptyMap();
            } else {
                return configMap;
            }
        }
    }



}
