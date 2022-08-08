package org.ajlib;

import lombok.extern.slf4j.Slf4j;
import org.ajlib.util.ConfigUtil;
import org.ajlib.util.YamlUtil;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.ServiceLoader;

@Slf4j
public class Agent {
    public static void premain(String args, Instrumentation inst) {
        final ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class);
        try {
            final Map<String, Object> config = ConfigUtil.loadConfigAsMap();
            for (Plugin plugin : plugins) {
                plugin.initialize(YamlUtil.dump(plugin.name(), config));
                plugin.transformers().forEach(inst::addTransformer);
            }
        } catch (Exception e) {
            log.error("Error run agent", e);
        }

    }
}
