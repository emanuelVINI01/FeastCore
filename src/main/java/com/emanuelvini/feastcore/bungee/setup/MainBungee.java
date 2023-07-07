package com.emanuelvini.feastcore.bungee.setup;

import com.emanuelvini.feastcore.bungee.setup.loader.dependencies.BungeeDependencyFinder;
import com.emanuelvini.feastcore.bungee.setup.loader.plugin.BungeePluginFinder;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import com.emanuelvini.feastcore.common.storage.MySQL;
import com.emanuelvini.feastcore.common.storage.configuration.MySQLConfiguration;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.sqlprovider.connector.SQLConnector;
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

    @Getter
    private static BridgeLogger bridgeLogger;

    @Override
    public void onEnable() {
        super.onEnable();
        bungeePluginInstance =  this;

        val dependencyFinder = new BungeeDependencyFinder(this);
        val pluginFinder = new BungeePluginFinder();
        bridgeLogger = new BridgeLogger(false);


        SQLConnector mysql;

        try {
            mysql = MySQL.of(
                    MySQLConfiguration.builder().
                            host(host).port(port).database(database).username(username).password(password).build()
            );
            bridgeLogger.log(
                    "§aMySQL inicializado com sucesso!"
            );
        } catch (Exception e) {
            bridgeLogger.log(
                    "§cOcorreu um erro ao inicializar o MySQL. Verifique os dados na configurações."
            );

            bridgeLogger.log("§cInforme o erro abaixo: ");
            e.printStackTrace();
            getProxy().stop("MySQL Inválido ----> FeastCore");
            return;
        }



        instance = new MainFeast(dependencyFinder, null, pluginFinder, mysql, bridgeLogger, false);
        instance.enable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (instance != null) {
            instance.disable();
        }
    }
}
