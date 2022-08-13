package org.ajlib.log;

import org.ajlib.Agent;
import org.ajlib.util.ConfigUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHandlerTest {
    private static final String TEST_CONFIG_FILE = "config-log-test.yaml";

    @SuppressWarnings("unchecked")
    @Test
    void testNewInstance() throws IOException {
        System.setProperty(Agent.HOME, "/tmp");
        Map<String, Object> config = ConfigUtil.loadConfigAsMap(TEST_CONFIG_FILE);
        FileHandler fileHandler = new FileHandler((Map<String, Object>) config.get(LoggerFactory.LogType.FILE.toString().toLowerCase()));
        assertEquals("xxx.txt", fileHandler.getName());
    }

}