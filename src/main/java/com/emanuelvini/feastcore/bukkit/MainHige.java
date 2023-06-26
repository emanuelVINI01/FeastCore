package com.emanuelvini.feastcore.bukkit;

import com.emanuelvini.feastcore.bukkit.api.plugin.config.ConfigurationAutoSaver;
import com.emanuelvini.feastcore.bukkit.loader.finder.DependencyFinder;
import com.emanuelvini.feastcore.bukkit.loader.finder.PluginFinder;
import com.emanuelvini.feastcore.bukkit.loader.storage.MySQL;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MainHige extends JavaPlugin {

    private DependencyFinder dependencyFinder;

    private PluginFinder pluginFinder;

    private ConfigurationAutoSaver configurationAutoSaver;

    private HikariDataSource mysql;

    @Getter
    private static MainHige instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        super.onEnable();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §bInicializando MySQL...");

        try {
            mysql = new MySQL(getConfig()).connect();
            Bukkit.getConsoleSender().
                    sendMessage(
                            "§9[FeastCore] §aMySQL inicializado com sucesso!"
                    );
        } catch (Exception e) {
            Bukkit.getConsoleSender().
                    sendMessage(
                            "§9[FeastCore] §cOcorreu um erro ao inicializar o MySQL. Verifique os dados na configurações."
                    );

            Bukkit.getConsoleSender().
                    sendMessage("§9[FeastCore] §cInforme o erro abaixo: ");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §6Inicializando plugins...");
        dependencyFinder = new DependencyFinder(this);
        dependencyFinder.addDependency("NBTAPI", "https://cdn-raw.modrinth.com/data/nfGCP9fk/versions/wtKsBSun/item-nbt-api-plugin-2.11.3.jar");
        pluginFinder = new PluginFinder(this);
        configurationAutoSaver = new ConfigurationAutoSaver();
        pluginFinder.loadAll();
        dependencyFinder.downloadAndLoadDependencies();
        pluginFinder.enableAll();
        Bukkit.getConsoleSender().
                sendMessage(
                        "§9[FeastCore] §aPlugins inicializados com sucesso!"
                );
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §eDesabilitando plugins...");
        pluginFinder.disableAll();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §eDesabilitando dependências...");
        dependencyFinder.disableAll();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §eSalvando configurações customizadas...");
        configurationAutoSaver.saveAllConfigurations();
        Bukkit.getConsoleSender().
                sendMessage("§9[FeastCore] §cPlugin desligado com sucesso!");
    }
}