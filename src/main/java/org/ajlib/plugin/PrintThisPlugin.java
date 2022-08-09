package org.ajlib.plugin;

import org.ajlib.NamedClassTransformer;
import org.ajlib.Plugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class PrintThisPlugin implements Plugin {
    @Override
    public String name() {
        return "printThis";
    }

    @Nonnull
    @Override
    public List<NamedClassTransformer> transformers() {
        return Collections.singletonList(new PrintThisTransformer());
    }
}
