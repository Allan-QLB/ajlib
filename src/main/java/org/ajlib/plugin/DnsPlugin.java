package org.ajlib.plugin;

import org.ajlib.NamedClassTransformer;
import org.ajlib.Plugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class DnsPlugin implements Plugin {
    private NamedClassTransformer transformer;
    @Override
    public String name() {
        return "dns";
    }

    @Override
    public void initialize(String config) {
        transformer = new DnsResolveTransformer();
        transformer.initialize(config);
    }

    @Nonnull
    @Override
    public List<NamedClassTransformer> transformers() {
        return Collections.singletonList(transformer);
    }
}
