package org.ajlib.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.ajlib.Agent;
import org.ajlib.exception.ConfigurationException;
import org.ajlib.rule.Rule;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ConfigUtil {
    private static final String CONFIG_FILE = "config.yaml";

    public static Map<String, Object> loadConfigAsMap() throws IOException {
        return loadConfigAsMap(CONFIG_FILE);
    }

    public static List<Rule> parseRules(String config) {
        List<Map<String,Object>> list = YamlUtil.loadAs(config, List.class);
        return list.stream().map(m -> new Rule(m)).collect(Collectors.toList());
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
