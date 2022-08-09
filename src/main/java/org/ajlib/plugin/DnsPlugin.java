package org.ajlib.plugin;

import org.ajlib.NamedClassTransformer;
import org.ajlib.Plugin;

import javax.annotation.Nonnull;
import java.util.List;

public class DnsPlugin implements Plugin {
    @Override
    public String name() {
        return null;
    }

    @Override
    public void initialize(String config) {

    }

    @Nonnull
    @Override
    public List<NamedClassTransformer> transformers() {
        return null;
    }
}
