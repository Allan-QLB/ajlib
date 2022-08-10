package org.ajlib.plugin;

import org.ajlib.NamedClassTransformer;
import org.ajlib.log.LogHelper;
import org.ajlib.log.Logger;
import org.ajlib.rule.DnsFilter;
import org.ajlib.util.ConfigUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class DnsResolveTransformer implements NamedClassTransformer {

    private static final Logger LOG = LogHelper.getLogger(DnsResolveTransformer.class);

    @Override
    public void initialize(String config) {
        DnsFilter.initialize(ConfigUtil.parseRules(config));
    }

    @Override
    public String targetPattern() {
        return "java/net/InetAddress";
    }

    @Override
    public String name() {
        return "dns";
    }

    @Override
    public byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (method.name.equals("getByName")
                    && method.desc.equals("(Ljava/lang/String;)Ljava/net/InetAddress;")) {
                LOG.error("target found {} {}", loader, className, new RuntimeException());
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                        "org/ajlib/rule/DnsFilter", "filter",
                        "(Ljava/lang/String;)V", false));
                method.instructions.insert(insnList);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
