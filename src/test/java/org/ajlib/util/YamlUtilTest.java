package org.ajlib.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlUtilTest {

    @Test
    void testDumpMap() {
        Map<String, Object> map = new HashMap<>();
        final List<String> sourceList = Arrays.asList("hello", "world");
        final HashMap<String, Object> sourceMap = new HashMap<String, Object>() {
            {
                put("xx", 20);
                put("m", "mm");
            }
        };
        map.put("a", sourceList);
        map.put("b", sourceMap);
        final String a = YamlUtil.dump("a", map);
        final String b = YamlUtil.dump("b", map);
        final List<String> aList = YamlUtil.loadAs(a, List.class);
        final Map<String, Object> bMap = YamlUtil.loadAs(b, Map.class);
        assertEquals(sourceList, aList);
        assertEquals(sourceMap, bMap);
    }

}