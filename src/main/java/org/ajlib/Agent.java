package org.ajlib;

import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.ajlib.log.ConsoleLog;
import org.ajlib.util.ConfigUtil;
import org.ajlib.util.YamlUtil;

import java.io.Console;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;

public class Agent {
    public static final String HOME = "agent.home";
    public static void premain(String args, Instrumentation inst) {
        try {
            final Map<String, Object> config = ConfigUtil.loadConfigAsMap();
            loadPlugins(inst, config);
            loadClassPathPlugins(inst, config);
        } catch (Throwable t) {
            ConsoleLog.error("Error run agent", t);
        }
    }

    private static void loadClassPathPlugins(Instrumentation inst, Map<String, Object> config) {
        final ServiceLoader<Plugin> plugins = ServiceLoader.load(Plugin.class);
        for (Plugin plugin : plugins) {
            ConsoleLog.info("Load classpath plugin {}", plugin);
            plugin.initialize(YamlUtil.dump(plugin.name(), config));
            plugin.transformers().forEach(inst::addTransformer);
        }
    }

    @SneakyThrows
    private static void loadPlugins(Instrumentation inst, Map<String, Object> config) {
        final String agentJarLocation = Agent.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        System.setProperty(Agent.HOME, agentJarLocation);
        final File agentFile = new File(agentJarLocation);
        inst.appendToBootstrapClassLoaderSearch(new JarFile(agentFile));
        final CompositeTransformer compositeTransformer = new CompositeTransformer(inst);
        final Set<Class<?>> classes = ClassUtil.scanPackage("org.ajlib.plugin");
        for (Class<?> clazz : classes) {
            if (!ClassUtil.isAbstract(clazz) && ClassUtil.isAssignable(Plugin.class, clazz)) {
                final Plugin plugin = (Plugin) clazz.newInstance();
                plugin.initialize(YamlUtil.dump(plugin.name(), config));
                compositeTransformer.addTransformers(plugin.transformers());
                ConsoleLog.info("Loaded plugin {}", plugin);
            }
        }
        inst.addTransformer(compositeTransformer, true);
        inst.setNativeMethodPrefix(compositeTransformer, "ajcompose");
        for (Class<?> loadedClass : inst.getAllLoadedClasses()) {
            if (inst.isModifiableClass(loadedClass) && compositeTransformer.canTransform(loadedClass)) {
                inst.retransformClasses(loadedClass);
            }
        }

        final File agentParent = agentFile.getParentFile();
        final File pluginsDir = new File(agentParent, "plugins");
        final File[] files = pluginsDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
//                final JarFile jarFile = new JarFile(file);
//                final Manifest manifest = jarFile.getManifest();
                //todo load from dir
            }
        }
    }
}
