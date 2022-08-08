package org.ajlib.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilTest {

    private static final String CFG_FILE = "config-test.yaml";

    @Test
    void testLoadConfig() throws IOException {
        final Map<String, Object> config = ConfigUtil.loadConfigAsMap(CFG_FILE);
        assertNotNull(config);
    }



}