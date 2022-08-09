package org.ajlib.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.ajlib.Agent;
import org.ajlib.exception.ConfigurationException;
import org.ajlib.exception.RuleException;
import org.ajlib.rule.Rule;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConfigUtil {
    private static final String CONFIG_FILE = "config.yaml";

    public static Map<String, Object> loadConfigAsMap() throws IOException {
        return loadConfigAsMap(CONFIG_FILE);
    }

    @SuppressWarnings("unchecked")
    static Map<String, Object> loadConfigAsMap(String fileName) throws IOException {
        final InputStream configStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
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

    @SuppressWarnings("unchecked")
    public static <R extends Rule> List<R> parseRules(String config, Class<R> clazz) {
        List<Map<String,Object>> ruleMapList = (List<Map<String,Object>>) YamlUtil.loadAs(config, List.class);
        final Constructor<R> constructor = ReflectUtil.getConstructor(clazz, Map.class);
        List<R> rules = new ArrayList<>();
        for (Map<String, Object> ruleMap : ruleMapList) {
            try {
                final R rule = constructor.newInstance(ruleMap);
                rules.add(rule);
            } catch (InstantiationException | IllegalAccessException  | InvocationTargetException e) {
                throw new RuleException("Can not create instance of " + clazz, e);
            }
        }
        return rules;
    }

    public static List<Rule> parseRules(String config) {
        return parseRules(config, Rule.class);
    }
}
