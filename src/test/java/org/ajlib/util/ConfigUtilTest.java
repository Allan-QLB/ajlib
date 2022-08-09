package org.ajlib.util;

import org.ajlib.rule.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilTest {

    private static final String CFG_FILE = "config-test.yaml";

    @Test
    void testLoadConfig() throws IOException {
        final Map<String, Object> config = ConfigUtil.loadConfigAsMap(CFG_FILE);
        System.out.println(config);
        assertNotNull(config);
    }

    @Test
    void testParseRules() throws IOException {
        Map<String, Object> config = ConfigUtil.loadConfigAsMap(CFG_FILE);
        List<Rule> rules = ConfigUtil.parseRules(YamlUtil.dump("a", config));
        assertNotNull(rules);
        assertFalse(rules.isEmpty());
    }



}