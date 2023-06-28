package com.emanuelvini.feastcore.bukkit.api.plugin;

import com.emanuelvini.feastcore.bukkit.MainFeast;
import com.emanuelvini.feastcore.bukkit.api.plugin.config.CustomConfiguration;
import com.emanuelvini.feastcore.bukkit.api.plugin.log.Logging;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.val;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Consumer;

public class FeastPlugin extends JavaPlugin {

    @Getter
    private static FeastPlugin instance;

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

    public final void addDependency(String name, String url) {
        MainFeast.getInstance().getDependencyFinder().
                addDependency(name, url);
    }

    public final void awaitEvent(Event event, Consumer<Event> consumer) {
        MainFeast.getInstance().getEventFinder().awaitEvent(
                event,
                consumer,
                this
        );
    }


    public final HikariDataSource getMySQL() {
        return MainFeast.getInstance().getMysql();
    }


    public final CustomConfiguration getCustomConfiguration(String name, boolean autoSave) {
        val configurationFile =
                new File(getDataFolder(), name);
        val customConfiguration =
                new CustomConfiguration(configurationFile, this);
        if (autoSave) MainFeast.getInstance().
                getConfigurationAutoSaver().
                addConfigurationToAutoSave(customConfiguration, this);
        return customConfiguration;
    }

    
}
