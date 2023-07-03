package com.emanuelvini.feastcore.common.loader;

import com.emanuelvini.feastcore.common.loader.dependecies.IDependencyFinder;
import com.emanuelvini.feastcore.common.loader.events.IEventFinder;
import com.emanuelvini.feastcore.common.loader.plugin.IPluginFinder;
import com.emanuelvini.feastcore.common.logging.BridgeLogger;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;

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

        logger.log("§9[FeastCore] §6Inicializando dependências...");

        //Adicionando as dependências necessárias pelo Core


        if (bukkit)
            dependencyFinder.addDependency("NBTAPI", "https://cdn-raw.modrinth.com/data/nfGCP9fk/versions/wtKsBSun/item-nbt-api-plugin-2.11.3.jar");


        pluginFinder.loadAll();

        dependencyFinder.downloadAndLoadDependencies();

        pluginFinder.enableAll();

        logger.log("§9[FeastCore] §aPlugins inicializados com sucesso!");
    }


    public void disable() {
        logger.log("§9[FeastCore] §eDesabilitando plugins...");
        pluginFinder.disableAll();
        logger.log("§9[FeastCore] §eDesabilitando dependências...");
        dependencyFinder.disableAll();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §cPlugin desligado com sucesso!");
    }
}
