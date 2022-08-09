package org.ajlib;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.regex.Pattern;

public interface NamedClassTransformer extends ClassFileTransformer {

    void initialize(String config);

    String targetPattern();

    String name();

    default boolean shouldTransform(String className) {
       return Pattern.matches(targetPattern(), className);
    }

    byte[] doTransform(ClassLoader loader,
                     String className,
                     Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain,
                     byte[] classfileBuffer) throws IllegalClassFormatException;

    @Override
    default byte[] transform(ClassLoader loader,
                     String className,
                     Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain,
                     byte[] classfileBuffer) throws IllegalClassFormatException {
        if (shouldTransform(className)) {
            return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        }
        return classfileBuffer;
    }
}
