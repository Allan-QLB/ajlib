package org.ajlib.rule;

public interface Matching {
    String name();
    boolean matches(Object value, Object content);
}
