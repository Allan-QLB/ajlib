package org.ajlib.rule;

import cn.hutool.core.util.ClassUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Matchings {
    private static final Map<String, Matching> REGISTERED = new HashMap<>();
    static {
        Set<Class<?>> classes = ClassUtil.scanPackage("org.ajlib.rule");
        for (Class<?> clazz : classes) {
            if (Matching.class.isAssignableFrom(clazz)) {
                try {
                    Class.forName(clazz.getName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void register(Matching matching) {
        REGISTERED.put(matching.name(), matching);
    }

    public static Matching of(String name) {
        return REGISTERED.get(name);
    }
}
