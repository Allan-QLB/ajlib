package org.ajlib.rule;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DnsFilterTest {

    @Test
    void filter() {
        List<Rule> rules = Arrays.asList(new Rule(new HashMap<String, Object>() {
            {
                put(Rule.KEY_MATCH, "eq");
                put(Rule.KEY_RULE, "aaa");
            }
        }));
        DnsFilter.initialize(rules);
        assertThrows(UnknownHostException.class, () -> DnsFilter.filter("aaa"));
    }
}