package org.ajlib.plugin;

import org.ajlib.NamedClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PrintThisTransformer implements NamedClassTransformer {
    @Override
    public void initialize(String config) {

    }

    @Override
    public String targetPattern() {
        return "com/google/gson/LinkedTreeMap";
    }

    @Override
    public String name() {
        return "printThis";
    }

    @Override
    public byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("put")
                    && method.desc.equals("(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                        "org/ajlib/rule/PrintFilter", "print",
                        "(Ljava/lang/Object;)V", false));
                method.instructions.insert(insnList);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
