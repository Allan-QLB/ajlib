package org.ajlib.rule;

import org.ajlib.exception.RuleException;

import javax.annotation.Nonnull;
import java.util.Map;

public class Rule {
    public static final String KEY_MATCH = "matching";
    public static final String KEY_RULE = "rule";

    @Nonnull
    private final Matching matching;
    private Object rule;

    public Rule(@Nonnull Map<String, Object> ruleContent) {
        matching = parseMatching(ruleContent);
        parseRule(ruleContent);
    }

    protected void parseRule(Map<String, Object> ruleContent) {
        Object r = ruleContent.get(KEY_RULE);
        if (r == null) {
            throw new RuleException("Empty rule found");
        }
        rule = r;
    }

    @Nonnull
    public Matching parseMatching(Map<String, Object> ruleContent) {
        String matchingName = (String) ruleContent.get(KEY_MATCH);
        Matching registered = Matchings.of(matchingName);
        if (registered == null) {
            throw new RuleException("Unsupported matching " + matchingName);
        }
        return registered;
    }

    public boolean test(Object value) {
        return matching.matches(value, rule);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "matching=" + matching +
                ", rule=" + rule +
                '}';
    }
}
