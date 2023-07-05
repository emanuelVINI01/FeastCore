package com.emanuelvini.feastcore.bukkit.setup;


import com.emanuelvini.feastcore.bukkit.setup.loader.dependecies.DependencyFinder;
import com.emanuelvini.feastcore.bukkit.setup.loader.events.EventFinder;
import com.emanuelvini.feastcore.bukkit.setup.loader.plugin.PluginFinder;
import com.emanuelvini.feastcore.common.loader.MainFeast;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import com.emanuelvini.feastcore.common.storage.MySQL;
import com.emanuelvini.feastcore.common.storage.configuration.MySQLConfiguration;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MainBukkit extends JavaPlugin {


    @Getter
    private static MainFeast instance;

    @Getter
    private static BridgeLogger brideLogger;

    @Getter
    private static MainBukkit bukkitPluginInstance;

    @Override
    public void onEnable() {
        bukkitPluginInstance = this;
        saveDefaultConfig();

        brideLogger = new BridgeLogger(true);

        if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") == null ||
                !Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit").isEnabled()) {

            brideLogger.log("§c§lERRO FATAL! §cFastAsyncWorldEdit não encontrado.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;

        }



        brideLogger.log("§bInicializando MySQL...");

        SQLConnector mysql;

        try {
            val mysqlSection = getConfig().getConfigurationSection("mysql");
            mysql = MySQL.of(MySQLConfiguration.builder().
                    host(mysqlSection.getString("host")).
                    port(mysqlSection.getInt("port")).
                    database(mysqlSection.getString("database")).
                    username(mysqlSection.getString("user")).
                    password(mysqlSection.getString("password")).build()
            );
            brideLogger.log(
                            "§aMySQL inicializado com sucesso!"
                    );
        } catch (Exception e) {
            brideLogger.log(
                            "§cOcorreu um erro ao inicializar o MySQL. Verifique os dados na configurações."
                    );

            brideLogger.log("§cInforme o erro abaixo: ");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        val dependencyFinder = new DependencyFinder(this);


        val pluginFinder = new PluginFinder(this);
        val eventFinder = new EventFinder();

        instance = new MainFeast(dependencyFinder, eventFinder, pluginFinder, mysql, brideLogger, true);
        instance.enable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (instance != null && bukkitPluginInstance != null) {
            instance.disable();
        }
    }
}
