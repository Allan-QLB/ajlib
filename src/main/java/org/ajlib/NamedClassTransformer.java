package org.ajlib;

import org.ajlib.matching.ClassNameMatching;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

public interface NamedClassTransformer extends ClassFileTransformer {

    void initialize(String config);

    @Nonnull
    List<ClassNameMatching> matches();

    String name();

    default boolean shouldTransform(String className) {
        for (ClassNameMatching matching : matches()) {
            if (matching.matches(className)) {
                return true;
            }
        }
        return false;
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
