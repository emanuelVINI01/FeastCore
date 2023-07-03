package com.emanuelvini.feastcore.bungee.setup.loader.util;


import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import lombok.SneakyThrows;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Handler;

public class PluginManager {

    @SneakyThrows
    public static Plugin loadPlugin(File pluginFile) {
        val description = getPluginDescription(pluginFile);
        return createPluginInstance(pluginFile, description);
    }

    @SneakyThrows
    private static PluginDescription getPluginDescription(File pluginFile) {
        ProxyServer proxy = ProxyServer.getInstance();
        net.md_5.bungee.api.plugin.PluginManager pluginmanager = proxy.getPluginManager();

        JarFile jar = new JarFile(pluginFile);
        JarEntry pdf = jar.getJarEntry("bungee.yml");
        if (pdf == null) {
            pdf = jar.getJarEntry("plugin.yml");
        }
        try (InputStream in = jar.getInputStream(pdf)) {
            PluginDescription pluginDescription = new Yaml().loadAs(in, PluginDescription.class);
            pluginDescription.setFile(pluginFile);
            HashSet<String> plugins = new HashSet<>();
            for (Plugin plugin : pluginmanager.getPlugins()) {
                plugins.add(plugin.getDescription().getName());
            }
            for (String dependency : pluginDescription.getDepends()) {
                if (!plugins.contains(dependency)) {
                    throw new IllegalArgumentException(MessageFormat.format("Missing plugin dependency {0}", dependency));
                }
            }
            return pluginDescription;
        }

    }

    @SneakyThrows
    public static void unloadPlugin(Plugin plugin) {


        val pluginClassLoader = plugin.getClass().getClassLoader();

        val pluginManager = ProxyServer.getInstance().getPluginManager();

            plugin.onDisable();


            for (Handler handler : plugin.getLogger().getHandlers()) {
                handler.close();
            }
            pluginManager.unregisterListeners(plugin);
        pluginManager.unregisterCommands(plugin);
            ProxyServer.getInstance().getScheduler().cancel(plugin);
            plugin.getExecutorService().shutdownNow();
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getClass().getClassLoader() == pluginClassLoader) {
                    thread.interrupt();
                    thread.join(2000);
                    if (thread.isAlive()) {
                        thread.stop();
                    }
            }
        }


            Map<String, Command> commandMap = ReflectionUtils.getFieldValue(pluginManager, "commandMap");
        commandMap.entrySet().removeIf(entry -> entry.getValue().getClass().getClassLoader() == pluginClassLoader);

        //remove plugin ref from internal plugins map
            ReflectionUtils.<Map<String, Plugin>>getFieldValue(pluginManager, "plugins").values().remove(plugin);

        //close classloader
        if (pluginClassLoader instanceof URLClassLoader) {
                ((URLClassLoader) pluginClassLoader).close();
        }

            ReflectionUtils.<Set<ClassLoader>>getStaticFieldValue(pluginClassLoader.getClass(), "allLoaders").remove(pluginClassLoader);
        }



    @SneakyThrows
    private static Plugin createPluginInstance(File pluginFile, PluginDescription pluginDescription) {
        val proxy = ProxyServer.getInstance();

        Class<?> pluginClassLoaderClass = MainBungee.getBungeePluginInstance().getClass().getClassLoader().getClass();
        ClassLoader pluginClassLoader = null;
        for (Constructor<?> constructor : pluginClassLoaderClass.getDeclaredConstructors()) {
            ReflectionUtils.setAccessible(constructor);
            Parameter[] parameters = constructor.getParameters();
            if (
                    (parameters.length == 3) &&
                            parameters[0].getType().isAssignableFrom(ProxyServer.class) &&
                            parameters[1].getType().isAssignableFrom(PluginDescription.class) &&
                            parameters[2].getType().isAssignableFrom(URL[].class)
            ) {
                pluginClassLoader = (ClassLoader) constructor.newInstance(proxy, pluginDescription, new URL[] {pluginFile.toURI().toURL()});
                break;
            } else if (
                    (parameters.length == 4) &&
                            parameters[0].getType().isAssignableFrom(ProxyServer.class) &&
                            parameters[1].getType().isAssignableFrom(PluginDescription.class) &&
                            parameters[2].getType().isAssignableFrom(File.class) &&
                            parameters[3].getType().isAssignableFrom(ClassLoader.class)
            ) {
                pluginClassLoader = (ClassLoader) constructor.newInstance(proxy, pluginDescription,pluginFile , null);
                break;
            } else if (
                    (parameters.length == 1) &&
                            parameters[0].getType().isAssignableFrom(URL[].class)
            ) {
                pluginClassLoader = (ClassLoader) constructor.newInstance(new Object[] {new URL[] {pluginFile.toURI().toURL()}});
                break;
            }
        }
        if (pluginClassLoader == null) {
            throw new IllegalStateException(MessageFormat.format(
                    "Unable to create PluginClassLoader instance, no suitable constructors found in class {0} constructors {1}",
                    pluginClassLoaderClass, Arrays.toString(pluginClassLoaderClass.getDeclaredConstructors())
            ));
        }
        return (Plugin)
                pluginClassLoader
                        .loadClass(pluginDescription.getMain())
                        .getDeclaredConstructor()
                        .newInstance();
    }

}
