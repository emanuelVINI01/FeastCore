package com.emanuelvini.higecore.bukkit.api.plugin.config;

import com.emanuelvini.higecore.bukkit.api.plugin.HigePlugin;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomConfiguration extends YamlConfiguration {

    @Getter
    protected File file;
    private HigePlugin plugin;



    public CustomConfiguration(File file, HigePlugin plugin) {
        this.plugin = plugin;
        this.file = file;
        try {
            if (!file.exists()) {
                InputStream customConfig = plugin.getResource(file.getName());
                byte[] buffer = new byte[customConfig.available()];
                customConfig.read(buffer);

                Files.write(buffer, file);
            }
            loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(String.format("§e[HigeCore] §cOcorreu um erro ao carregar uma configuração customizado do plugin §f%s§c:", file.getName()));
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void save(File file) {
        try {
            super.save(file);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(String.format("§e[HigeCore] §cOcorreu um erro ao salvar uma configuração customizado do plugin §f%s§c:", file.getName()));
            e.printStackTrace();
        }

    }
}
