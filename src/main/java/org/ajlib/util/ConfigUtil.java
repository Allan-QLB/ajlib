package org.ajlib.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import org.ajlib.exception.ConfigurationException;
import org.ajlib.exception.RuleException;
import org.ajlib.rule.Rule;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfigUtil {
    private static final String CONFIG_FILE = "config.yaml";
    private static final String DEFAULT_CONFIG_DIR = "config";

    private ConfigUtil() {}

    public static Map<String, Object> loadConfigAsMap() throws IOException {
        return loadConfigAsMap(CONFIG_FILE);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadConfigAsMap(String fileName) throws IOException {
        InputStream configStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
        if (configStream == null) {
            File file = new File(Env.agentBaseDir() + File.separator + DEFAULT_CONFIG_DIR, fileName);
            if (file.exists() && file.isFile()) {
                configStream = Files.newInputStream(file.toPath());
            } else {
                throw new ConfigurationException("Config file " + fileName + " not found");
            }
        }
        final Yaml yaml = new Yaml();
        try (InputStream inputStream = configStream) {
            final Map<String, Object> configMap = yaml.loadAs(inputStream, Map.class);
            if (MapUtil.isEmpty(configMap)) {
                return Collections.emptyMap();
            } else {
                return configMap;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <R extends Rule> List<R> parseRules(String config, Class<R> clazz) {
        List<Map<String,Object>> ruleMapList = YamlUtil.loadAs(config, List.class);
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
