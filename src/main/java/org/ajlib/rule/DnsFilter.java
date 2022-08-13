package org.ajlib.rule;

import org.ajlib.log.LoggerFactory;
import org.ajlib.log.Logger;

import java.net.UnknownHostException;
import java.util.List;

public class DnsFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DnsFilter.class);
    private static List<Rule> rules;

    public static void initialize(List<Rule> rules) {
        DnsFilter.rules = rules;
    }

    public static void filter(String host) throws UnknownHostException {
        for (Rule rule : rules) {
            if (rule.test(host)) {
                LOGGER.debug("host {} matches rule {}", host, rule);
                LOGGER.info("host {} matches rule {}", host, rule);
                LOGGER.warn("host {} matches rule {}", host, rule);
                LOGGER.error("host {} matches rule {}", host, rule, new IllegalArgumentException("xxxxxx"));
                throw new UnknownHostException();
            }
        }
    }

}
