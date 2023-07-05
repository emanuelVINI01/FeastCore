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
        
        logger.log("§e\n" +
                "___________                     __    _________                        ____   ________      .________\n" +
                "\\_   _____/___ _____    _______/  |_  \\_   ___ \\  ___________   ____   \\   \\ /   /_   |     |   ____/\n" +
                " |    __)/ __ \\\\__  \\  /  ___/\\   __\\ /    \\  \\/ /  _ \\_  __ \\_/ __ \\   \\   Y   / |   |     |____  \\ \n" +
                " |     \\\\  ___/ / __ \\_\\___ \\  |  |   \\     \\___(  <_> )  | \\/\\  ___/    \\     /  |   |     /       \\\n" +
                " \\___  / \\___  >____  /____  > |__|    \\______  /\\____/|__|    \\___  >    \\___/   |___| /\\ /______  /\n" +
                "     \\/      \\/     \\/     \\/                 \\/                   \\/                   \\/        \\/ \n");

        logger.log("§e\n" +
                "_______________.___. ___________   _____      _____    _______   ____ ______________.____ ____   ____.___ _______  .___ \n" +
                "\\______   \\__  |   | \\_   _____/  /     \\    /  _  \\   \\      \\ |    |   \\_   _____/|    |\\   \\ /   /|   |\\      \\ |   |\n" +
                " |    |  _//   |   |  |    __)_  /  \\ /  \\  /  /_\\  \\  /   |   \\|    |   /|    __)_ |    | \\   Y   / |   |/   |   \\|   |\n" +
                " |    |   \\\\____   |  |        \\/    Y    \\/    |    \\/    |    \\    |  / |        \\|    |__\\     /  |   /    |    \\   |\n" +
                " |______  // ______| /_______  /\\____|__  /\\____|__  /\\____|__  /______/ /_______  /|_______ \\___/   |___\\____|__  /___|\n" +
                "        \\/ \\/                \\/         \\/         \\/         \\/                 \\/         \\/                   \\/     \n");
        
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
        logger.log("§eDesabilitando plugins...");
        pluginFinder.disableAll();
        logger.log("§eDesabilitando dependências...");
        dependencyFinder.disableAll();
        Bukkit.getConsoleSender().
                sendMessage("§cPlugin desligado com sucesso!");
    }
}
