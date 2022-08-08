package org.ajlib;

import javax.annotation.Nonnull;
import java.util.List;

public interface Plugin {
    String name();
    void initialize(String config);
    @Nonnull
    List<NamedClassTransformer> transformers();
}
