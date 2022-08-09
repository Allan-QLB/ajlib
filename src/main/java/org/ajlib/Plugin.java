package org.ajlib;

import javax.annotation.Nonnull;
import java.util.List;

public interface Plugin {
    String name();
    default void initialize(String config) {}
    @Nonnull
    List<NamedClassTransformer> transformers();
}
