package org.ajlib;

import cn.hutool.core.util.ClassUtil;
import lombok.SneakyThrows;
import org.ajlib.log.Logger;
import org.ajlib.log.LoggerFactory;
import org.ajlib.util.ConfigUtil;
import org.ajlib.util.YamlUtil;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Agent {
    private static Logger logger;
    private static final String AGENT_JAR_LOCATION;
    private static final JarFile agentJar;
    public static final String HOME = "agent.home";
    private static final String PLUGIN_ENTRY = "Ajlib-Entry";
    private static final String INTERNAL_PLUGIN_PACKAGE = "org.ajlib.plugin";

    static {
        AGENT_JAR_LOCATION = Agent.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        System.setProperty(Agent.HOME, AGENT_JAR_LOCATION);
        final File agentFile = new File(AGENT_JAR_LOCATION);
        try {
            agentJar = new JarFile(agentFile);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot open agent jar file");
        }
    }

    private static void initialize(Instrumentation instrumentation) {
        instrumentation.appendToBootstrapClassLoaderSearch(agentJar);
        logger = LoggerFactory.getLogger(Agent.class);
    }

    public static void premain(String args, Instrumentation inst) {
        try {
            initialize(inst);
            final Map<String, Object> config = ConfigUtil.loadConfigAsMap();
            loadPlugins(inst, config);
            loadClassPathPlugins(inst, config);
        } catch (Throwable t) {
            logger.error("Error run agent", t);
        }
    }

    private static void loadClassPathPlugins(Instrumentation inst, Map<String, Object> config) {
        //todo
    }

    @SneakyThrows
    private static void loadPlugins(Instrumentation inst, Map<String, Object> config) {
        final CompositeTransformer rootTransformer = new CompositeTransformer(inst);
        loadPluginsInternal(config, rootTransformer);
        loadPluginsExternal(new File(AGENT_JAR_LOCATION).getParentFile(), inst, config, rootTransformer);
        inst.addTransformer(rootTransformer, true);
        inst.setNativeMethodPrefix(rootTransformer, "ajcompose");
        for (Class<?> loadedClass : inst.getAllLoadedClasses()) {
            if (inst.isModifiableClass(loadedClass) && rootTransformer.canTransform(loadedClass)) {
                inst.retransformClasses(loadedClass);
            }
        }
    }

    private static void loadPluginsExternal(File agentParent,
                                            Instrumentation inst,
                                            Map<String, Object> config,
                                            CompositeTransformer root) {
        final File pluginsDir = new File(agentParent, "plugins");
        final File[] files = pluginsDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                try (final JarFile jarFile = new JarFile(file)) {
                    final Manifest manifest = jarFile.getManifest();
                    String entryPoint = manifest.getMainAttributes().getValue(PLUGIN_ENTRY);
                    if (entryPoint != null) {
                        inst.appendToSystemClassLoaderSearch(jarFile);
                        Class<?> entryClass = Class.forName(entryPoint);
                        if (Plugin.class.isAssignableFrom(entryClass)) {
                            Plugin plugin = (Plugin) entryClass.getDeclaredConstructor().newInstance();
                            plugin.initialize(YamlUtil.dump(plugin.name(), config));
                            root.addTransformers(plugin.transformers());
                            logger.info("Load plugin of class {}", entryClass);
                        }
                    }
                } catch (ClassNotFoundException | NoSuchMethodException
                         | InstantiationException | IllegalAccessException
                         | InvocationTargetException | IOException e) {
                    logger.error("Error load plugin from file {}", file, e);
                }
            }
        }
    }

    private static void loadPluginsInternal(Map<String, Object> config,
                                            CompositeTransformer root)  {
        final Set<Class<?>> classes = ClassUtil.scanPackage(INTERNAL_PLUGIN_PACKAGE);
        for (Class<?> clazz : classes) {
            if (!ClassUtil.isAbstract(clazz) && ClassUtil.isAssignable(Plugin.class, clazz)) {
                try {
                    final Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                    plugin.initialize(YamlUtil.dump(plugin.name(), config));
                    root.addTransformers(plugin.transformers());
                    logger.info("Loaded plugin {}", plugin);
                } catch (InstantiationException | NoSuchMethodException |
                         IllegalAccessException | InvocationTargetException e) {
                    logger.error("Error load plugin {}", clazz, e);
                }
            }
        }
    }
}
