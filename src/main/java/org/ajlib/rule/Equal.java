package org.ajlib.rule;

import java.util.Objects;

public class Equal implements Matching {
    static {
        Matchings.register(new Equal());
    }
    @Override
    public String name() {
        return "eq";
    }

    @Override
    public boolean matches(Object value, Object content) {
        return Objects.equals(value, content);
    }
}
