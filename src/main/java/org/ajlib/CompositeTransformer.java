package org.ajlib;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class CompositeTransformer implements ClassFileTransformer {

    private final Instrumentation instrumentation;
    private final List<NamedClassTransformer> transformers = new ArrayList<>();

    public CompositeTransformer(@Nonnull Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        for (ClassFileTransformer transformer : transformers) {
            classfileBuffer = transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }
        return classfileBuffer;
    }


    public boolean canTransform(Class<?> clazz) {
        if (!instrumentation.isModifiableClass(clazz)) {
            return false;
        }
        final String internalName = clazz.getName().replace(".", "/");
        for (NamedClassTransformer transformer : transformers) {
            if (transformer.shouldTransform(internalName)) {
                return true;
            }
        }
        return false;
    }

    public void addTransformers(List<NamedClassTransformer> transformers) {
        this.transformers.addAll(transformers);
    }

}
