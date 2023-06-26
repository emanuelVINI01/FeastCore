package com.emanuelvini.feastcore.bukkit.api.plugin.config;


import com.emanuelvini.feastcore.bukkit.api.plugin.HigePlugin;
import lombok.AllArgsConstructor;


import java.util.HashMap;

@AllArgsConstructor
public class ConfigurationAutoSaver {

    private final HashMap<CustomConfiguration, HigePlugin>
            autoSaveEnabled = new HashMap<>();


    public void addConfigurationToAutoSave(CustomConfiguration configuration,
                                           HigePlugin plugin) {
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
