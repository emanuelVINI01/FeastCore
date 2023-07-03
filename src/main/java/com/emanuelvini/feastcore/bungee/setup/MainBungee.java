package com.emanuelvini.feastcore.bungee.setup;

import com.emanuelvini.feastcore.bungee.setup.loader.dependencies.DependencyFinder;
import com.emanuelvini.feastcore.bungee.setup.loader.plugin.PluginFinder;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import com.emanuelvini.feastcore.common.storage.MySQL;
import com.emanuelvini.feastcore.common.storage.configuration.MySQLConfiguration;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import lombok.Getter;
import lombok.val;
import net.md_5.bungee.api.plugin.Plugin;

@ConfigFile("config.yml")

public class MainBungee extends Plugin {

    @Getter
    private static MainFeast instance;

    @Getter
    private static MainBungee bungeePluginInstance;

    @ConfigField("mysql.host") private String host;

    @ConfigField("mysql.port")  private int port;

    @ConfigField("mysql.database") private String database;

    @ConfigField("mysql.user") private String username;

    @ConfigField("mysql.password") private String password;



    @Override
    public void onEnable() {
        super.onEnable();
        bungeePluginInstance =  this;

        val dependencyFinder = new DependencyFinder(this);
        val pluginFinder = new PluginFinder();


        val mysql = MySQL.of(
                MySQLConfiguration.builder().
                        host(host).port(port).database(database).username(username).password(password).build()
        );
        instance = new MainFeast(dependencyFinder, null, pluginFinder, mysql, new BridgeLogger(false), false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (instance != null) {
            instance.disable();
        }
    }
}
