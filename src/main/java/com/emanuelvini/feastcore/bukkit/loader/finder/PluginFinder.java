package com.emanuelvini.feastcore.bukkit.loader.finder;

import com.emanuelvini.feastcore.bukkit.MainFeast;
import com.emanuelvini.feastcore.bukkit.api.plugin.FeastPlugin;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PluginFinder {

    private MainFeast plugin;

    private final Map<String, FeastPlugin> loadedPlugins = new HashMap<>();

    private final Map<String, FeastPlugin> enabledPlugins = new HashMap<>();

    public void loadAll() {
        val pluginsDirectory = new File(plugin.getDataFolder(), "plugins");
        if (!pluginsDirectory.exists()) pluginsDirectory.mkdirs();
        for (File pluginFile : pluginsDirectory.listFiles()) {
            if (pluginFile.isDirectory()) continue;
            loadPlugin(pluginFile);
        }
    }

    public void enableAll() {
        for (String name : loadedPlugins.keySet()) {
            enablePlugin(name);
        }
    }

    public void disableAll() {
        for (String name : enabledPlugins.keySet()) {
            disablePlugin(name);
        }
    }

    protected void disablePlugin(String name) {
        val plugin = enabledPlugins.get(name);
        if (plugin != null) {
            try {
                Bukkit.getPluginManager().disablePlugin(plugin);
                loadedPlugins.put(name, plugin);
                enabledPlugins.remove(name);
                Bukkit.getConsoleSender().sendMessage(String.format("§9[FeastCore] §aPlugin §f%s§a desabilitado com sucesso!", name));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(String.format("§9[FeastCore] §cOcorreu um erro ao desabilitar o plugin §f%s§c:", name));
                e.printStackTrace();
            }
        }
    }

    protected void enablePlugin(String name) {
        try {

            val plugin = loadedPlugins.get(name);
            Bukkit.getPluginManager().enablePlugin(plugin);
            loadedPlugins.remove(name);
            enabledPlugins.put(name, plugin);
            Bukkit.getConsoleSender().
                    sendMessage(String.format(
                            "§9[FeastCore] §aPlugin §f%s§a habilitado com sucesso!",
                            name));
        } catch (Exception e) {
            Bukkit.getConsoleSender().
                    sendMessage(String.format(
                            "§9[FeastCore] §cOcorreu um erro ao habilitar o plugin §f%s§c:",
                            name));
            e.printStackTrace();
        }
    }

    protected void loadPlugin(File file) {
        try {

            val plugin = Bukkit.getPluginManager().loadPlugin(file);
            ;
            if (!(plugin instanceof FeastPlugin)) {
                Bukkit.getConsoleSender().
                        sendMessage(String.format(
                                "§9[FeastCore] §cOcorreu um erro ao carregar o plugin §f%s§c. Ele não e um plugin Feast.",
                                file.getName()));
                try {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                } catch (Exception ignore) {
                }
                return;
            }
            val higePlugin = (FeastPlugin) plugin;
            higePlugin.setupDependencies();
            loadedPlugins.put(plugin.getName(), higePlugin);
            Bukkit.getConsoleSender().
                    sendMessage(String.format(
                            "§9[FeastCore] §aPlugin §f%s§a carregado com sucesso!",
                            file.getName()));

        } catch (Exception e) {
            Bukkit.getConsoleSender().
                    sendMessage(String.format(
                            "§9[FeastCore] §cOcorreu um erro ao carregar o plugin §f%s§c:",
                            file.getName()));
            e.printStackTrace();
        }

    }

}
