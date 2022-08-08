package org.ajlib.matching;

public abstract class BaseMatching implements ClassNameMatching {
    protected final String target;

    public BaseMatching(String target) {
        this.target = target;
    }
}
