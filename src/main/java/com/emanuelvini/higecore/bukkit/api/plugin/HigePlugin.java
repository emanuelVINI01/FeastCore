package com.emanuelvini.higecore.bukkit.api.plugin;

import com.emanuelvini.higecore.bukkit.MainHige;
import com.emanuelvini.higecore.bukkit.api.plugin.config.CustomConfiguration;
import com.emanuelvini.higecore.bukkit.api.plugin.log.Logging;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HigePlugin extends JavaPlugin {

    @Getter
    private static HigePlugin instance;

    @Getter
    private final Logging customLogger = new Logging(this);

    public void setupDependencies() {}

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        saveConfig();
        instance = null;
    }

    public final void addDependency(String name, String url) { MainHige.getInstance().getDependencyFinder().addDependency(name, url); }

    public final HikariDataSource getMySQL() {
        return MainHige.getInstance().getMysql();
    }


    public final CustomConfiguration getCustomConfiguration(String name, boolean autoSave) {
        val configurationFile = new File(getDataFolder(), name);
        val customConfiguration = new CustomConfiguration(configurationFile, this);
        if (autoSave) MainHige.getInstance().getConfigurationAutoSaver().addConfigurationToAutoSave(customConfiguration, this);
        return customConfiguration;
    }

    
}
