package com.emanuelvini.feastcore.common.loader;

import com.emanuelvini.feastcore.common.loader.dependecies.IDependencyFinder;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import com.emanuelvini.feastcore.common.loader.plugin.IPluginFinder;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MainFeast {

    private IDependencyFinder dependencyFinder;

    private IEventFinder eventFinder;

    private IPluginFinder pluginFinder;

    private SQLConnector mysql;

    private BridgeLogger logger;

    private boolean bukkit;

    @Getter
    private static MainFeast instance;

    public void enable() {

        instance = this;


        logger.log("\n" +
                "§5______                    _     _____                      _   _  __  \n" +
                "§5|  ___|                  | |   /  __ \\                    | | | |/  | \n" +
                "§5| |_     ___   __ _  ___ | |_  | /  \\/  ___   _ __   ___  | | | |`| | \n" +
                "§5|  _|   / _ \\ / _` |/ __|| __| | |     / _ \\ | '__| / _ \\ | | | | | | \n" +
                "§5| |    |  __/| (_| |\\__ \\| |_  | \\__/\\| (_) || |   |  __/ \\ \\_/ /_| |_\n" +
                "§5\\_|     \\___| \\__,_||___/ \\__|  \\____/ \\___/ |_|    \\___|  \\___/ \\___/\n" +
                "                                                                      \n" +
                "                                                                      \n");



        
        logger.log("§6Inicializando dependências...");
        //Adicionando as dependências necessárias pelo Core

        if (bukkit)
            dependencyFinder.addDependency("NBTAPI", "https://cdn-raw.modrinth.com/data/nfGCP9fk/versions/wtKsBSun/item-nbt-api-plugin-2.11.3.jar");


        pluginFinder.loadAll();

        dependencyFinder.downloadAndLoadDependencies();

        pluginFinder.enableAll();

        logger.log("§aPlugins inicializados com sucesso!");
    }


    public void disable() {
        logger.log("\n" +
                "§c______                    _     _____                      _   _  __  \n" +
                "§c|  ___|                  | |   /  __ \\                    | | | |/  | \n" +
                "§c| |_     ___   __ _  ___ | |_  | /  \\/  ___   _ __   ___  | | | |`| | \n" +
                "§c|  _|   / _ \\ / _` |/ __|| __| | |     / _ \\ | '__| / _ \\ | | | | | | \n" +
                "§c| |    |  __/| (_| |\\__ \\| |_  | \\__/\\| (_) || |   |  __/ \\ \\_/ /_| |_\n" +
                "§c\\_|     \\___| \\__,_||___/ \\__|  \\____/ \\___/ |_|    \\___|  \\___/ \\___/\n" +
                "                                                                      \n" +
                "                                                                      \n");

        logger.log("§eDesabilitando plugins...");
        pluginFinder.disableAll();
        logger.log("§eDesabilitando dependências...");
        dependencyFinder.disableAll();
        logger.log("§cPlugin desligado com sucesso!");
    }
}
