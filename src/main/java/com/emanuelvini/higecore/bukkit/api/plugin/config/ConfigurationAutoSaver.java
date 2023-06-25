package com.emanuelvini.higecore.bukkit.api.plugin.config;

import com.emanuelvini.higecore.bukkit.MainHige;
import com.emanuelvini.higecore.bukkit.api.plugin.HigePlugin;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.HashMap;

@AllArgsConstructor
public class ConfigurationAutoSaver {

    private final HashMap<CustomConfiguration, HigePlugin> autoSaveEnabled = new HashMap<>();

    private MainHige plugin;

    public void addConfigurationToAutoSave(CustomConfiguration configuration, HigePlugin plugin) {
        autoSaveEnabled.put(configuration, plugin);
    }

    public void saveAllConfigurations() {
        for (CustomConfiguration configuration : autoSaveEnabled.keySet()) {
            saveConfiguration(configuration);
        }
    }

    private void saveConfiguration(CustomConfiguration configuration) {
        configuration.save(configuration.getFile());
    }

}
