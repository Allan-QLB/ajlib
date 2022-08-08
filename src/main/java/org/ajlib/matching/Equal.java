package org.ajlib.matching;

import java.util.Objects;

public class Equal extends BaseMatching {

    public Equal(String target) {
        super(target);
    }

    @Override
    public String name() {
        return "eq";
    }

    @Override
    public boolean matches(String name) {
        return Objects.equals(name, target);
    }
}
