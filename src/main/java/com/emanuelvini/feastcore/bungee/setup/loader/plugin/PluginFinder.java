package com.emanuelvini.feastcore.bungee.setup.loader.plugin;


import com.emanuelvini.feastcore.bungee.api.BungeeFeastPlugin;
import com.emanuelvini.feastcore.bungee.setup.MainBungee;
import com.emanuelvini.feastcore.bungee.setup.loader.util.PluginManager;
import com.emanuelvini.feastcore.common.loader.plugin.IPluginFinder;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PluginFinder implements IPluginFinder {

    private Plugin plugin;

    private final Map<String, BungeeFeastPlugin> loadedPlugins = new HashMap<>();

    private final Map<String, BungeeFeastPlugin> enabledPlugins = new HashMap<>();

    @Override
    public void loadAll() {
        val pluginsDirectory = new File(plugin.getDataFolder(), "plugins");
        if (!pluginsDirectory.exists()) pluginsDirectory.mkdirs();
        for (File pluginFile : pluginsDirectory.listFiles()) {
            if (pluginFile.isDirectory()) continue;
            loadPlugin(pluginFile);
        }
    }

    @Override
    public void enableAll() {
        for (String name : loadedPlugins.keySet()) {
            enablePlugin(name);
        }
    }


    @Override
    public void disableAll() {
        for (String name : enabledPlugins.keySet()) {
            disablePlugin(name);
        }
    }

    protected void disablePlugin(String name) {
        val plugin = enabledPlugins.get(name);
        if (plugin != null) {
            try {
                PluginManager.unloadPlugin(plugin);
                loadedPlugins.put(name, plugin);
                enabledPlugins.remove(name);
                MainBungee.getInstance().getLogger().log(String.format("§9[FeastCore] §aPlugin §f%s§a desabilitado com sucesso!", name));
            } catch (Exception e) {
                MainBungee.getInstance().getLogger().log(String.format("§9[FeastCore] §cOcorreu um erro ao desabilitar o plugin §f%s§c:", name));
                e.printStackTrace();
            }
        }
    }

    protected void enablePlugin(String name) {
        try {

            val plugin = loadedPlugins.get(name);
            plugin.onEnable();
            loadedPlugins.remove(name);
            enabledPlugins.put(name, plugin);
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(String.format(
                    "§9[FeastCore] §aPlugin §f%s§a habilitado com sucesso!",
                    name)));
        } catch (Exception e) {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(String.format(
                    "§9[FeastCore] §cOcorreu um erro ao habilitar o plugin §f%s§c:",
                    name)));
            e.printStackTrace();
        }
    }

    protected void loadPlugin(File file) {
        try {

            val plugin = PluginManager.loadPlugin(file);

            if (!(plugin instanceof BungeeFeastPlugin)) {
                MainBungee.getInstance().getLogger().log(String.format(
                                "§9[FeastCore] §cOcorreu um erro ao carregar o plugin §f%s§c. Ele não e um plugin Feast.",
                                file.getName()));
                try {
                    PluginManager.unloadPlugin(plugin);
                } catch (Exception ignore) {
                }
                return;
            }
            val feastPlugin = (BungeeFeastPlugin) plugin;
            feastPlugin.setupDependencies();
            feastPlugin.onLoad();
            loadedPlugins.put(plugin.getDescription().getName(), feastPlugin);
            MainBungee.getInstance().getLogger().log(String.format(
                            "§9[FeastCore] §aPlugin §f%s§a carregado com sucesso!",
                            file.getName()));

        } catch (Exception e) {
            MainBungee.getInstance().getLogger().log(String.format(
                            "§9[FeastCore] §cOcorreu um erro ao carregar o plugin §f%s§c:",
                            file.getName()));
            e.printStackTrace();
        }

    }

}
