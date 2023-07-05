package com.emanuelvini.feastcore.bukkit.setup.loader.plugin;

import com.emanuelvini.feastcore.bukkit.api.BukkitFeastPlugin;
import com.emanuelvini.feastcore.common.api.FeastPlugin;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.emanuelvini.feastcore.common.loader.plugin.IPluginFinder;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class PluginFinder implements IPluginFinder {

    private JavaPlugin plugin;

    private static BridgeLogger logger = MainFeast.getInstance().getLogger();

    private final Map<String, BukkitFeastPlugin> loadedPlugins = new HashMap<>();

    private final Map<String, BukkitFeastPlugin> enabledPlugins = new HashMap<>();




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
                Bukkit.getPluginManager().disablePlugin(plugin);
                loadedPlugins.put(name, plugin);
                enabledPlugins.remove(name);
                Bukkit.getConsoleSender().sendMessage(String.format("§aPlugin §f%s§a desabilitado com sucesso!", name));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(String.format("§cOcorreu um erro ao desabilitar o plugin §f%s§c:", name));
                e.printStackTrace();
            }
        }
    }

    protected void enablePlugin(String name) {
        try {

            val plugin = loadedPlugins.get(name);
            if (plugin.isEnabled()) return;
            Bukkit.getPluginManager().enablePlugin(plugin);
            loadedPlugins.remove(name);
            enabledPlugins.put(name, plugin);
            logger.log(String.format(
                            "§aPlugin §f%s§a habilitado com sucesso!",
                            name));
        } catch (Exception e) {
            logger.log(String.format(
                            "§cOcorreu um erro ao habilitar o plugin §f%s§c:",
                            name));
            e.printStackTrace();
        }
    }

    protected void loadPlugin(File file) {
        try {

            val plugin = Bukkit.getPluginManager().loadPlugin(file);

            if (!(plugin instanceof BukkitFeastPlugin)) {
                Bukkit.getConsoleSender().
                        sendMessage(String.format(
                                "§cOcorreu um erro ao carregar o plugin §f%s§c. Ele não e um plugin Feast.",
                                file.getName()));
                try {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                } catch (Exception ignore) {
                }
                return;
            }
            val feastPlugin = (BukkitFeastPlugin) plugin;
            feastPlugin.setupDependencies();
            loadedPlugins.put(plugin.getName(), feastPlugin);
            logger.log(String.format(
                            "§aPlugin §f%s§a carregado com sucesso!",
                            file.getName()));

        } catch (Exception e) {
            logger.log(String.format(
                            "§cOcorreu um erro ao carregar o plugin §f%s§c:",
                            file.getName()));
            e.printStackTrace();
        }

    }

}
