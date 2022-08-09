package org.ajlib.rule;

import java.net.UnknownHostException;
import java.util.List;

public class DnsFilter {
    private static List<Rule> rules;

    public static void initialize(List<Rule> rules) {
        DnsFilter.rules = rules;
    }

    public static void filter(String host) throws UnknownHostException {
        for (Rule rule : rules) {
            if (rule.test(host)) {
                System.out.println("host " + host + "matched");
                throw new UnknownHostException();
            }
        }
    }

}
